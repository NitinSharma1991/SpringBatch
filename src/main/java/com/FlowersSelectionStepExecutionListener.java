package com;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class FlowersSelectionStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Before our Step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Executing step after");
        String flowerType = stepExecution.getJobParameters().getString("date");
        return flowerType.equalsIgnoreCase("roses") ? new ExitStatus("Trim Required") : new ExitStatus("No Trim Required");
    }
}
