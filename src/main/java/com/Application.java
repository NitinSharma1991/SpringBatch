package com;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@SpringBootApplication
@EnableBatchProcessing
public class Application {

    public static String INSERT_ORDER_SQL = "insert into "
            + "SHIPPED_ORDER_OUTPUT(order_id, first_name, last_name, email, item_id, item_name, cost, ship_date)"
            + " values(orderId,firstName,:lastName,:email,:itemId,:itemName,:cost,:shipDate)";
    //    private static String sql = "Select * from shipped_order order by order_id";
    private static String sql = "select order_id, first_name, last_name, "
            + "email, cost, item_id, item_name, ship_date ";
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    DataSource dataSource;
    @Autowired
    ObjectMapper objectMapper;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Step chunkBasedStep() throws Exception {
        return this.stepBuilderFactory.get("chunkBasedStep")
                .<Order, TrackedOrder>chunk(10)
                .reader(itemReader())
                .processor(compositeItemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Order, TrackedOrder> compositeItemProcessor() {

        return new CompositeItemProcessorBuilder<Order, TrackedOrder>()
                .delegates(itemProcessor(), trackedOrderItemProcessor())
                .build();
    }

    @Bean
    public ItemProcessor<Order, TrackedOrder> trackedOrderItemProcessor() {

        return new TrackedOrderItemProcessor();
    }

    @Bean
    public ItemProcessor<Order, Order> itemProcessor() {
        BeanValidatingItemProcessor<Order> itemProcessor = new BeanValidatingItemProcessor<Order>();
        itemProcessor.setFilter(true);
        return itemProcessor;
    }

    @Bean
    public ItemWriter<TrackedOrder> itemWriter() {

        return new JsonFileItemWriterBuilder<TrackedOrder>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<TrackedOrder>())
                .resource(new FileSystemResource("shipped_orders_output.json"))
                .name("JsonWriter")
                .build();

        /*System.out.println("Item Writer");
        return new JdbcBatchItemWriterBuilder<Order>()
                .dataSource(dataSource)
                .sql(INSERT_ORDER_SQL)
                .beanMapped()
//                .itemPreparedStatementSetter(new OrderItemPreparedStatementSetter())
                .build();*/
    }

    /*  @Bean
      public ItemReader<Order> itemReader() {
          return new JdbcCursorItemReaderBuilder<Order>()
                  .dataSource(dataSource)
                  .name("jdbcCursorItemReader")
                  .sql(sql)
                  .rowMapper(new OrderRowMapper())
                  .build();
      }*/
    @Bean
    public ItemReader<Order> itemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Order>()
                .dataSource(dataSource)
                .name("jdbcCursorItemReader")
                .queryProvider(queryProvider())
                .rowMapper(new OrderRowMapper())
                .pageSize(10)
                .build();
    }

    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean bean = new SqlPagingQueryProviderFactoryBean();
        bean.setSelectClause(sql);
        bean.setFromClause("from SHIPPED_ORDER");
        bean.setSortKey("order_id");
        bean.setDataSource(dataSource);
        return bean.getObject();

    }

    @Bean
    public Job chunkBasedJob() throws Exception {
        return jobBuilderFactory.get("chunkBasedJob1")
                .start(chunkBasedStep()).build();
    }


}
