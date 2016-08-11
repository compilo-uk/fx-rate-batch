package com.sharpe.capital.task;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sharpe.capital.data.access.api.model.cassandra.FxTick;
import com.sharpe.capital.data.access.api.repository.FxTickRepository;
import com.sharpe.capital.fetcher.RateFetcher;
import com.sharpe.capital.fetcher.TrueFxFetcher;
import com.sharpe.capital.model.FxRate;

import scala.collection.JavaConversions;
import scala.collection.mutable.Buffer;

/**
 * This task utilizes the **fx-rate-importer** project and **data-access-api**
 * project to fetch real-time currency rates from True FX and persist them in
 * the platforms NoSQL Cassandra data store. The method saveTicks executes every
 * 250ms, and persists each tick on its own thread to minimize latency and
 * improve write speeds.
 */
@Component
public class UpdateFxRates {

	private static final RateFetcher fetcher = new TrueFxFetcher();

	private static final FxTickRepository fxTickRepository = FxTickRepository.getInstance();

	private static final ExecutorService executor = Executors.newFixedThreadPool(10);

	@Scheduled(fixedRate = 250)
	public void saveTicks() {

		String[] currencyCodes = { "EUR/USD", "GBP/USD", "AUD/USD", "USD/JPY", "USD/CAD" };

		Buffer<FxRate> rates = fetcher.getRatesBySymbols(currencyCodes);

		JavaConversions.bufferAsJavaList(rates).stream().forEach(rate -> this.saveRate(rate));

	}

	/**
	 * Saves an FxRate object in Cassandra on a new Thread
	 * 
	 * @param rate
	 *            the FxRate object
	 */
	private void saveRate(FxRate rate) {
		executor.execute(() -> {
			fxTickRepository.save(new FxTick(rate.symbol(), rate.date(), BigDecimal.valueOf(rate.ask().doubleValue()),
					BigDecimal.valueOf(rate.bid().doubleValue())));
		});
	}

}
