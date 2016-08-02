package com.sharpe.capital.task;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.sharpe.capital.fetcher.RateFetcher;
import com.sharpe.capital.fetcher.TrueFxFetcher;
import com.sharpe.capital.model.FxRate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateFxRates implements Tasklet {

	private static final RateFetcher fetcher = new TrueFxFetcher();

	@Override
	public RepeatStatus execute(StepContribution stepConfig, ChunkContext chunkContext) throws Exception {

		FxRate rate = fetcher.getRateBySymbol("AUD/USD");

		log.info("Symbol: " + rate.symbol());
		log.info("Date: " + rate.date());
		log.info("Ask: " + rate.ask());
		log.info("Bid: " + rate.bid());

		return RepeatStatus.FINISHED;

	}

}
