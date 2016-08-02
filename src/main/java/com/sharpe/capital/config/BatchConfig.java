package com.sharpe.capital.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sharpe.capital.task.UpdateFxRates;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final String UPDATE_FX_RATES_JOB = "updateFxRatesJob";

	private static final String STEP_1 = "step1";

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job updateFxRatesJob() {
		return jobBuilderFactory.get(UPDATE_FX_RATES_JOB)
				.flow(stepBuilderFactory.get(STEP_1).tasklet(new UpdateFxRates()).build()).end().build();
	}
}