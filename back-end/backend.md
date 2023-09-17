# Back-End Structure

## The Module

- micr-parent:
  - Parent module. To manage the version of dependencies respectively.
- micr-web
  - The application that user directly accesses. The consumer of dubbo
- micr-dataservice
  - The provider of dubbo. To manipulate and operate with database.
- micr-api
  - Consist of the service interface and pojo that the whole project can invoke.
- micr-common
  - The common utils, Constant class.
- micr-translation
  - Translate the content
- micr-generation
  - Generate context by words

## Initial Settings

### Create module micr-web

- config the server port and context path and sort of configuration

- ```yaml
  #Config server port and context path
  spring:
    application:
      name: micr-web
    mvc:
      pathmatch:
        matching-strategy: ant_path_matcher
  server:
    port: 8001
    servlet:
      context-path: /ai
      encoding:
        charset: UTF-8
        enabled: true
        force: true
  dubbo:
    registry:
      address: zookeeper://localhost:2181
    scan:
      base-packages: com.ailearningvocabulary.bomnmi.micrweb
    consumer:
      check: false
      timeout: 50000
      retries: 0
  ```

- Use swagger to help generate the API document

  - ```java
    /**
     * @author Bomnmi
     * @version 1.0
     * @date 2023/7/27 22:22
     */
    @Configuration
    public class SwaggerConfiguration {
        //Create Docket
        @Bean
        public Docket docket(){
            Docket docket = new Docket(DocumentationType.SWAGGER_2);
    
            //Create API info, the whole description about api document
            ApiInfo apiInfo = new ApiInfoBuilder().title("AI Help Learning Vocabulary In Context").version("1.0")
                    .description("Combine Ai and vocabulary learning")
                    .contact(new Contact("Mo Zhou", null, "bomnmi123@gmail.com"))
                    .build();
    
            //Set and use ApiInfo
            docket = docket.apiInfo(apiInfo);
    
            //Set the package involving document generation
            docket = docket.select().apis(
                    RequestHandlerSelectors.basePackage("com.ailearningvocabulary.bomnmi.micrweb.controller"))
                    .build();
            return docket;
        }
    }
    ```


- Use spring-mail to help send email to the user:

  - ```yaml
    spring:
    #spring mail
      mail:
        host: smtp.gmail.com
        port: 587
        username: email
        password: password
        properties:
          mail:
            smtp:
              auth: true
              starttls:
                enable: true
    ```

  - ```java
    package com.ailearningvocabulary.bomnmi.web.service.impl;
    
    import com.ailearningvocabulary.bomnmi.web.service.EmailService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.mail.SimpleMailMessage;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.stereotype.Service;
    
    import java.util.Random;
    
    /**
     * @author Bomnmi
     * @version 1.0
     * @date 2023/7/29 23:22
     */
    @Service
    public class EmailServiceImpl implements EmailService {
        private Random random = new Random();
        @Autowired
        JavaMailSender javaMailSender;
        @Override
        public void sendVerificationCode(String toEmail) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Verification Code");
            StringBuilder verificationCode = new StringBuilder("");
            //Generate a 4 random digits code
            for (int i = 0; i < 4; i++) {
                verificationCode.append(random.nextInt(10));
            }
            mailMessage.setText(verificationCode.toString());
    
            javaMailSender.send(mailMessage);
        }
    }
    
    ```

- Create GlobalExceptionHandler to handle the exception that we want to catch.

- Create ParamFormatValidationAspect to do the validation for all controller whose parameter consist of email

### Create micr-dataservice

- Use mybatis and mybatis generator plugin to create corresponding mapper.xml and pojo class respectively.

  - ```xml
    <!--mybatis generator plugin-->
    <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.6</version>
        <configuration>
            <!--The location of configuration file-->
            <configurationFile>GeneratorMapper.xml</configurationFile>
            <verbose>true</verbose>
            <overwrite>true</overwrite>
        </configuration>
    </plugin>
    ```
  - GeneratorMapper.xml. Set the detail and configuration of mapper interfaces, mapper.xml and model class.

    - ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <!DOCTYPE generatorConfiguration
              PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
              "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
      <generatorConfiguration>
          <!-- mybatis driver location -->
          <classPathEntry location="C:\Users\62488\Maven\maven_repositroy\mysql\mysql-connector-java\8.0.30\mysql-connector-java-8.0.30.jar"/>
      
          <context id="tables" targetRuntime="MyBatis3">
              <!-- Do not generate documentation -->
              <commentGenerator>
                  <property name="suppressAllComments" value="true" />
              </commentGenerator>
      
              <!-- The configuration of connection to the mysql -->
              <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                              connectionURL="jdbc:mysql://localhost:3306/ai_vocabulary"
                              userId="root"
                              password="zhoumo123fabao">
              </jdbcConnection>
      
              <!-- Generate Model Class -->
              <javaModelGenerator targetPackage="com.ailearningvocabulary.bomnmi.dataservice.model"
                                  targetProject="D:\Uob-Project\Ai-help-learning-vocabulary-in-context\back-end\micr-dataservice\src\main\java">
                  <property name="enableSubPackages" value="false" />
                  <property name="trimStrings" value="false" />
              </javaModelGenerator>
              
              <!-- Generate Mapper.xml -->
              <sqlMapGenerator targetPackage="mappers" targetProject="D:\Uob-Project\Ai-help-learning-vocabulary-in-context\back-end\micr-dataservice\src\main\resources">
                  <property name="enableSubPackages" value="false" />
              </sqlMapGenerator>
              
              <!-- Generate Mapper Interfaces -->
              <javaClientGenerator type="XMLMAPPER" targetPackage="com.ailearningvocabulary.bomnmi.dataservice.mappers" targetProject="D:\Uob-Project\Ai-help-learning-vocabulary-in-context\back-end\micr-dataservice\src\main\java">
                  <property name="enableSubPackages" value="false" />
              </javaClientGenerator>
              
              <!-- The relation of table and pojo -->
              <table tableName="t_friend" domainObjectName="Friend"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
              <table tableName="t_paragraph" domainObjectName="Paragraph"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
              <table tableName="t_paragraph_vocabulary" domainObjectName="ParagraphVocabulary"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
              <table tableName="t_record" domainObjectName="Record"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
              <table tableName="t_user" domainObjectName="User"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
              <table tableName="t_user_vocabulary" domainObjectName="UserVocabulary"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
              <table tableName="t_vocabulary" domainObjectName="Vocabulary"
                     enableCountByExample="false"
                     enableUpdateByExample="false"
                     enableDeleteByExample="false"
                     enableSelectByExample="false"
                     selectByExampleQueryId="false"/>
          </context>
      </generatorConfiguration>
      ```
  
- Config dataservice, including datasource, mybatis and dubbo

  - ```yaml
    #server name
    spring:
      application:
        name: micr-dataservice
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/ai_vocabulary
        username: ****  #your own username
        password: ***** #your own password
        
    #Mybati configuration
    mybatis:
      mapper-locations: classpath:/mappers/**/*.xml
      configuration:
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
        map-underscore-to-camel-case: true
    #Dubbo Configuration
    dubbo:
      registry:
        address: zookeeper://localhost:2181
      scan:
        base-packages: com.bjpowernode.dataservice.service
      provider:
        timeout: 20000
        retries: 0
    ```

- Add redis cache for some service

  - Redis configuration

    - ```yaml
      #server name
      spring:
        application:
          name: micr-dataservice
        datasource:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/ai_vocabulary
          username: root
          password: zhoumo123fabao
        redis:
          database: 1
          host: localhost
          port: 6379
      ```

  - CacheManager configuration class

    - ```java
      @Configuration
      public class CustomizeRedisCacheManager {
          @Bean
          public CacheManager cacheManager(RedisConnectionFactory factory) {
              RedisSerializer<String> redisSerializer = new StringRedisSerializer();
              Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
              //Handle when querying cache occurs the convert problems
              ObjectMapper om = new ObjectMapper();
              om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
              om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
              jackson2JsonRedisSerializer.setObjectMapper(om);
              
              // Configure serialize
              RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                      .entryTtl(Duration.ofDays(1))
                      .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                      .disableCachingNullValues();
      
              RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                      .cacheDefaults(config)
                      .build();
              return cacheManager;
          }
      }
      
      ```

  - Add annotation to the service method that we want to add them into cache

    - ```java
      
      /**
      	@Cacheable: add it above query method
      	@CachePut: add it above insert method
      	@CacheEvict: add it above update or delete method
      */
      @DubboService(interfaceClass = UserVocabularyService.class, version = "1.0")
      @CacheConfig(cacheNames = "userVocabulary") // configure the namespace in Redis
      public class UserVocabularyServiceImpl implements UserVocabularyService {
      
          @Autowired
          UserVocabularyMapper userVocabularyMapper;
      
          @Override
          @Cacheable(key="'USER:ID:' + #p0" ) //key is the key in redis, #p0 means first arguement
          public List<UserVocabulary> selectAllUserVocabularyByUserId(String userId) {
              System.out.println("Cache not exist");
              return userVocabularyMapper.selectAllWordsByUserId(userId);
          }
      }
      
      ```

      

## Testing

- Use asertJ to help do the test

  - ```java
    package com.ailearningvocabulary.bomnmi.dataservice;
    import org.junit.jupiter.api.Test;
    import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
    /**
     * @author Bomnmi
     * @version 1.0
     * @date 2023/7/28 15:45
     */
    public class DemoTest {
        private Calculator underTest = new Calculator();
        @Test
        public void itShouldAddNumbers(){
            //given
            int numberOne = 20;
            int numberTwo = 30;
            //when
            int result = underTest.add(numberOne, numberTwo);
    
            //then
            int expected = 50;
            assertThat(result).isEqualTo(expected);
        }
        class Calculator{
            int add(int a, int b) {
                return a + b;
            }
        }
    }
    ```

- Use H2 database for memory database.

  - dependencies:

    - ```xml
      <!--h2-->
      <dependency>
          <groupId>com.h2database</groupId>
          <artifactId>h2</artifactId>
      </dependency>
      ```

  - configuration:

    - ```yaml
      spring:
        application:
          name: micr-dataservice
        datasource:
          driver-class-name: org.h2.Driver
          url: jdbc:h2:mem:ai_vocabulary;DB_CLOSE_DELAY=-1
          username: sa
          password: sa
        sql:
          init:
            schema-locations: classpath:db/schema-h2.sql
      ```

    - ```sql
      Drop Table if EXISTS t_user;
      CREATE TABLE t_user  (
           `id` varchar(32)  NOT NULL COMMENT 'The id of user, use UUID to gerneate',
           `username` varchar(255) NULL DEFAULT NULL COMMENT 'The name of user',
           `email` varchar(255)    NULL COMMENT 'The email of user',
           `password` varchar(32)  NULL COMMENT 'The password of user, use md5 to encryption',
           `create_time` datetime NULL DEFAULT NULL COMMENT 'The creat_time of account',
           `avatar_path` varchar(255)  NULL DEFAULT NULL COMMENT 'The url path refer to the avatar of user',
           PRIMARY KEY (`id`)
      );
      ```

    - Every Time we run the test, it would then recreate the table and test.

  - Use Mockito to do the mock mapper class, not need to authentically connect with the database, due to we have already done all the test about mapper before testing service

    - ```java
      package com.ailearningvocabulary.bomnmi.dataservice.service;
      import com.ailearningvocabulary.bomnmi.dataservice.mappers.VocabularyMapper;
      import org.junit.jupiter.api.Test;
      import org.junit.jupiter.api.extension.ExtendWith;
      import org.mockito.Mock;
      import org.mockito.junit.jupiter.MockitoExtension;
      
      /**
       * @author Bomnmi
       * @version 1.0
       * @date 2023/7/28 17:51
       */
      @ExtendWith(MockitoExtension.class)
      public class DemoService {
      
          /**
           * @Mock: is used when we test the service, because we have already test
           *          the mapper class so that we don't want to connect with the
           *          database again and again. We just need to make sure that
           *          the service class invoke the correct method in mapper. So
           *          that we use Mock to mock a action
           */
          @Mock
          private VocabularyMapper vocabularyMapper;
          @Test
          public void testMock(){
              // service invoke one method
              //when
              vocabularyService.getAllVocabulary()
              //then
        	    verify(vocabularyMapper).findAll();
              
              //if we want to test throwable error
              assertThatThrownBy(() -> underTest.addStudent(student))
                      .isInstanceOf(BadRequestException.class)
                      .hasMessageContaining("your message");
      
              //And also, we can decide the mock mapper class function return value
              given(studentRepository.selectExistsEmail(anyString()).willReturn(true));
              
              //this means this method in service never been invoked.
      		verify(vocabularyMapper, never).insert(anyString()).
          }
      }
      
      ```


## Space Repetition Algorithm(SM-2)

- Reference: https://www.supermemo.com/en/blog/application-of-a-computer-to-improve-the-results-obtained-in-working-with-the-supermemo-method

- EF-factor

  - Easiness factor reflecting the easiness of memorizing and retaining a given item in memory
  - From 1.3 - 2.5
    - 1.3 for most difficult items
    - 2.5 for the easiest ones

- I(n)

  - inter-repetition interval after the n-th repetition(in days)

- EF' = f(EF, q)

  - q:
    - the quality of this repetition
  - EF
    - the old Easiness factor
  - EF'
    - the updated Easiness factor

- The sequence of this algorithm

  - All words associate an E-factor equal to 2.5

  - Repeat words using the intervals:

    - ```
      I(1) = 1
      I(2) = 6
      for n > 2:
      	I(n) = I(n - 1) * EF
      ```

  - After each repetition, assess the quality of this repetition response in 0 -5 grade scale:

    - 5-- perfect response
    - 4--correct response after a hesitation
    - 3--correct response but hard to recall
    - 2--incorrect response, but the correct one seemed easy to recall
    - 1--incorrect response, but remember the correct one
    - 0--can't remember the word anymore, as the word is totally new

  - After each repetition, adjust the E-Factor of the recently repeated words according to the formula:

    - ```
      EF' = EF + (0.1 - (5 - q)*(0.08 + (5 - q)*0.02))
      ```

    - when q = 4, the EF factor would not be updated

    - If EF is less than 1.3, then let EF be 1.3

  - If the quality response was lower then 3 then start repetitions for the item from the beginning without changing the E-Factor. Rememorize the word as anew.

- In front-end, if the article contains the word that user added, then highlight it, and the modal window would provide 5 option for user to choose, which is the quality that we want to recalculate the EF

- In my project:

  - 5 -- quickly press "Yes" button when review one word, and the memorized definition is correct.
    - yes - yes

  - 4 -- press "Yes" button after 5 seconds but less then 10 seconds, the memorized definition is correct.
    - yes - yes

  - 3 -- press "Yes" button after 10 seconds, the memorized definition is correct.
    - yes - yes

  - 2 -- incorrect, press "No" or press "Yes" but the memorized definition is incorrect and the correct one seems easy to recall
    - no - yes
    - yes - no - yes

  - 1 -- incorrect, press "No" or press "Yes" but the memorized definition is incorrect and the correct one just only remember
    - no - yes
    - yes - no - yes

  - 0 -- totally forget the word.
    - no - no


## ChatGpt

- ```json
  // Gpt response
  {"contexts":
   [{
       "title":"Addicted to the Computer",
       "text":"Sarah was always on her computer, whether it was for work, social media, or streaming videos. Her friends and family were concerned about her addiction to the digital world. One day, she accidentally dropped her glass of water on the keyboard, causing the computer to malfunction. This incident made her realize the need to balance her online activities with real-life experiences."},
    {
        "title":"The Hat of Intrigue and Victory",
        "text":"Tom had always been an avid collector of hats. Among his collection was a unique hat that had special powers. Whenever he wore it, he felt a sudden surge of intrigue and confidence. One fateful day, he wore the hat to a job interview and aced it, securing a victory in landing his dream job. From that day onwards, the hat became his lucky charm, reminding him of the power of self-belief and determination."
    }
   ]
  }
  
  //java class
  AiContext{title='Addicted to the Computer', text='Sarah was always on her computer, whether it was for work, social media, or streaming videos. Her friends and family were concerned about her addiction to the digital world. One day, she accidentally dropped her glass of water on the keyboard, causing the computer to malfunction. This incident made her realize the need to balance her online activities with real-life experiences.'}
  AiContext{title='The Hat of Intrigue and Victory', text='Tom had always been an avid collector of hats. Among his collection was a unique hat that had special powers. Whenever he wore it, he felt a sudden surge of intrigue and confidence. One fateful day, he wore the hat to a job interview and aced it, securing a victory in landing his dream job. From that day onwards, the hat became his lucky charm, reminding him of the power of self-belief and determination.'}
  
  ```
  
- Front-end

  - ```json
    {
      "code": "1000",
      "message": "request success",
      "data": [
        {
          "id": null,
          "userId": null,
          "title": "Add Computer Glass",
          "createTime": null,
          "content": "As I was working on my computer, I accidentally knocked over my glass of water, causing it to spill all over the desk. I quickly grabbed a towel to add absorb the liquid and wiped down the computer and the glass to prevent any damage."
        },
        {
          "id": null,
          "userId": null,
          "title": "Hat Intrigue Victory",
          "createTime": null,
          "content": "In the middle of a crowded stadium, the magician pulled a rabbit out of his hat, creating a moment of intrigue among the audience. The gasps of excitement filled the air as he continued to perform his tricks, leading to a grand finale that resulted in a thunderous applause and his ultimate victory."
        }
      ],
      "accessToken": null
    }
    ```


## Vocabulary database

- Link : https://github.com/skywind3000/ECDICT/tree/master
- This database has over 3 million words, I split them into 26 tables according to the prefix character. From a - z in order to improve the ability of search.

## Pronunciation

- Api : https://dict.youdao.com/dictvoice?type=1&audio=hello
- Here is the YouDao Dictionary pronunciation api, where type = 0 means US, type = 1 means UK, and audio means which word that use to generate pronunciation.

## Quiz

- After user submit the setting, also need to send the index of words that user 
- When the user's repository contains at least 10 words, then can access this feature. The limitation of words that quiz can contain is 100.
- User could choose what dataset they want to do the quiz
  - Today words
    - Only when the number of today's word bigger than 10
  - All words
- User could choose a pattern that how the quiz word generate
  - Random
    - Would randomly choose the words
  - Based on added time
    - Would choose the words based on time, ascending or descending
  - Based on review times
    - Would choose the words based on the review times of each word, ascending or descending
  - Based on the difficulty level
    - Would choose the words based on the difficulty level, which is the EF factor, ascending or descending

# 2023.08.06

- Close the interceptor, in order do the test
- Mock chatgpt to generate tast, document part of code in method `addParagraphAndVocabulary`
- Task:
  - Finish the article generated feature in front-end
  - Finish the Lemmatization of words
  - Finish mark words in context and send it to front-end
  - ![image-20230807010006867](C:\Users\62488\AppData\Roaming\Typora\typora-user-images\image-20230807010006867.png)

# 2023.08.07

- Task:

  - Finish the highlight of front-end articles
  - There is a Set in front-end which record the words that have not been reviewed(Only when those unreviewed words been selected, the system would let the user to assess, give a quality response about this word and send it to back-end to do the space repetition calculation).
  - Add 2 new properties in t_paragraph: marked_content, read_status
    - marked_content: Mark the vocabulary which need to learn
    - read_status: Record whether this context has been read
  - Finish the keywords of article in front-end.
  - ![image-20230807235949698](C:\Users\62488\AppData\Roaming\Typora\typora-user-images\image-20230807235949698.png)

  - ![image-20230808000003945](C:\Users\62488\AppData\Roaming\Typora\typora-user-images\image-20230808000003945.png)

  - ![image-20230808000036008](C:\Users\62488\AppData\Roaming\Typora\typora-user-images\image-20230808000036008.png)

  - ![image-20230808000056018](C:\Users\62488\AppData\Roaming\Typora\typora-user-images\image-20230808000056018.png)

# 2023.08.08

- Task:
  - Finish the translation function