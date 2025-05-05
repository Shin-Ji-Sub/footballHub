package com.mezzala.config;

import com.mezzala.mapper.*;
import com.mezzala.service.*;
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
    public MypageService mypageService(MypageMapper mypageMapper) throws Exception {
        MypageServiceImpl mypageService = new MypageServiceImpl();
        mypageService.setMypageMapper(mypageMapper);
        return mypageService;
    }

    @Bean
    public NormalhubService normalhubService(NormalhubMapper normalhubMapper) throws Exception {
        NormalhubServiceImpl normalhubService = new NormalhubServiceImpl();
        normalhubService.setNormalhubMapper(normalhubMapper);
        return normalhubService;
    }

    @Bean
    public FandomhubService fandomhubService(FandomhubMapper fandomhubMapper) throws Exception {
        FandomhubServiceImpl fandomhubService = new FandomhubServiceImpl();
        fandomhubService.setFandomhubMapper(fandomhubMapper);
        return fandomhubService;
    }

    @Bean
    public RequestService requestService(RequestMapper requestMapper) throws Exception {
        RequestServiceImpl requestService = new RequestServiceImpl();
        requestService.setRequestMapper(requestMapper);
        return requestService;
    }

    @Bean
    public AdminService adminService(AdminMapper adminMapper) throws Exception {
        AdminServiceImpl adminService = new AdminServiceImpl();
        adminService.setAdminMapper(adminMapper);
        return adminService;
    }

    @Bean
    public ScheduleService scheduleService(ScheduleMapper scheduleMapper) throws Exception {
        ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();
        scheduleService.setScheduleMapper(scheduleMapper);
        return scheduleService;
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
