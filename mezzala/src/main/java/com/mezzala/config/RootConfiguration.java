package com.mezzala.config;

import com.mezzala.mapper.AccountMapper;
import com.mezzala.mapper.BoardMapper;
import com.mezzala.service.AccountService;
import com.mezzala.service.AccountServiceImpl;
import com.mezzala.service.BoardService;
import com.mezzala.service.BoardServiceImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.core.env.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.mezzala.mapper"})
@EnableTransactionManagement
public class RootConfiguration {
    @Autowired
    Environment env;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public AccountService accountService(AccountMapper accountMapper) throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setAccountMapper(accountMapper);
        return accountService;
    }

    @Bean
    public BoardService boardService(BoardMapper boardMapper) throws Exception {
        BoardServiceImpl boardService = new BoardServiceImpl();
        boardService.setBoardMapper(boardMapper);
        return boardService;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
        return dataSource;
    }

    @Bean
    SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return factoryBean.getObject();
    }

    @Bean
    PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager());
        return transactionTemplate;
    }
}
