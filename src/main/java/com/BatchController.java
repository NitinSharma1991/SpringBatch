/*
package com;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableBatchProcessing
public class BatchController {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @GetMapping("/loadJob")
    public HttpStatus load() {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder(jobExplorer);
        parametersBuilder.addString("date", String.valueOf("nitin"));
//        parametersBuilder.getNextJobParameters(deliverPackageJob());
        JobExecution jobExecution = null;
        try {
//            jobExecution = jobLauncher.run(deliverPackageJob(), parametersBuilder.toJobParameters());
            jobExecution = jobLauncher.run(prepareFlowerJob(), parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
        System.out.println(jobExecution.getStatus());
        return HttpStatus.OK;
    }

    @Bean
    public Job deliverPackageJob() {
        return this.jobBuilderFactory.get("deliverPackage")
//                .incrementer(new RunIdIncrementer())
                .start(packageStepItem())
                .next(driveToAddressStep()).on("FAILED").stop()
                .from(driveToAddressStep()).on("*").to(givePackageToCustomer())
                .end()
                .build();
    }

    @Bean
    public Step givePackageToCustomer() {
        return this.stepBuilderFactory.get("givePackageToCustomer")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Package Successfully delivered to Customer");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step returnToCompany() {
        return this.stepBuilderFactory.get("returnToCompany")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Package successfully returned to customer");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step driveToAddressStep() {
        boolean a = false;
        return this.stepBuilderFactory.get("driveToAddressStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        if (a) throw new Exception("Failed");
                        System.out.println("Succesfully Arrived");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step packageStepItem() {

        return this.stepBuilderFactory.get("packageItemStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
*/
/*                        String item = chunkContext.getStepContext()
                                .getJobParameters().get("item").toString();
                        String date = chunkContext.getStepContext()
                                .getJobParameters().get("run.date").toString();
                        System.out.println(String.format("The %s has been packaged on %s", item, date));*//*

                        System.out.println("Item Packaged");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step selectFlower() {
        boolean a = false;
        return this.stepBuilderFactory.get("selectFlower")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        if (a) throw new Exception("Failed");
                        System.out.println("Flower Selected");
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(selectFlowerListener())
                .build();
    }

    @Bean
    public Step arrangeFlower() {
        boolean a = false;
        return this.stepBuilderFactory.get("arrangeFlower")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        if (a) throw new Exception("Failed");
                        System.out.println("Flower Arranged");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public StepExecutionListener selectFlowerListener() {
        return new FlowersSelectionStepExecutionListener();
    }

    @Bean
    public Job prepareFlowerJob() {
        return this.jobBuilderFactory.get("prepareFlowerJob")
                .start(selectFlower()).on("Trim Required").to(removeThornStep()).next(arrangeFlower())
                .from(selectFlower()).on("No Trim Required").to(arrangeFlower())
                .end()
                .build();
    }

    private Step removeThornStep() {
        boolean a = false;
        return this.stepBuilderFactory.get("arrangeFlower")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        if (a) throw new Exception("Failed");
                        System.out.println("Thorn Removed");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

}
*/
