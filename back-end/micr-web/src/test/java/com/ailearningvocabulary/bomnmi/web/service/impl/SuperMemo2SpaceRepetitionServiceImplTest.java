package com.ailearningvocabulary.bomnmi.web.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SuperMemo2SpaceRepetitionServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SuperMemo2SpaceRepetitionServiceImplTest {
    @Autowired
    private SuperMemo2SpaceRepetitionServiceImpl superMemo2SpaceRepetitionServiceImpl;

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime() {
        UserVocabulary userVocabulary = new UserVocabulary();
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(10.0d);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(1);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        UserVocabulary actualCalculateEFAndNextReviewTimeResult = superMemo2SpaceRepetitionServiceImpl
                .calculateEFAndNextReviewTime(userVocabulary, 1);
        assertSame(userVocabulary, actualCalculateEFAndNextReviewTimeResult);
        assertEquals(1, actualCalculateEFAndNextReviewTimeResult.getReviewTimes().intValue());
    }

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime2() {
        UserVocabulary userVocabulary = mock(UserVocabulary.class);
        when(userVocabulary.getEf()).thenReturn(10.0d);
        when(userVocabulary.getReviewTimes()).thenReturn(1);
        when(userVocabulary.getNextReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(userVocabulary).setAddTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setEf((Double) any());
        doNothing().when(userVocabulary).setLastReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setNextReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setReviewTimes((Integer) any());
        doNothing().when(userVocabulary).setUserId((String) any());
        doNothing().when(userVocabulary).setWord((String) any());
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(10.0d);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(1);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        superMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(userVocabulary, 1);
        verify(userVocabulary).getEf();
        verify(userVocabulary).getReviewTimes();
        verify(userVocabulary).getNextReviewTime();
        verify(userVocabulary).setAddTime((LocalDateTime) any());
        verify(userVocabulary).setEf((Double) any());
        verify(userVocabulary).setLastReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setNextReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setReviewTimes((Integer) any());
        verify(userVocabulary).setUserId((String) any());
        verify(userVocabulary).setWord((String) any());
    }

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime3() {
        UserVocabulary userVocabulary = mock(UserVocabulary.class);
        when(userVocabulary.getEf()).thenReturn(10.0d);
        when(userVocabulary.getReviewTimes()).thenReturn(1);
        when(userVocabulary.getNextReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(userVocabulary).setAddTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setEf((Double) any());
        doNothing().when(userVocabulary).setLastReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setNextReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setReviewTimes((Integer) any());
        doNothing().when(userVocabulary).setUserId((String) any());
        doNothing().when(userVocabulary).setWord((String) any());
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(10.0d);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(1);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        superMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(userVocabulary, 3);
        verify(userVocabulary).getEf();
        verify(userVocabulary).getReviewTimes();
        verify(userVocabulary).getNextReviewTime();
        verify(userVocabulary).setAddTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setEf((Double) any());
        verify(userVocabulary).setLastReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setNextReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setReviewTimes((Integer) any());
        verify(userVocabulary).setUserId((String) any());
        verify(userVocabulary).setWord((String) any());
    }

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime4() {
        UserVocabulary userVocabulary = mock(UserVocabulary.class);
        when(userVocabulary.getEf()).thenReturn(0.1d);
        when(userVocabulary.getReviewTimes()).thenReturn(1);
        when(userVocabulary.getNextReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(userVocabulary).setAddTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setEf((Double) any());
        doNothing().when(userVocabulary).setLastReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setNextReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setReviewTimes((Integer) any());
        doNothing().when(userVocabulary).setUserId((String) any());
        doNothing().when(userVocabulary).setWord((String) any());
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(10.0d);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(1);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        superMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(userVocabulary, 3);
        verify(userVocabulary).getEf();
        verify(userVocabulary).getReviewTimes();
        verify(userVocabulary).getNextReviewTime();
        verify(userVocabulary).setAddTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setEf((Double) any());
        verify(userVocabulary).setLastReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setNextReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setReviewTimes((Integer) any());
        verify(userVocabulary).setUserId((String) any());
        verify(userVocabulary).setWord((String) any());
    }

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime5() {
        UserVocabulary userVocabulary = mock(UserVocabulary.class);
        when(userVocabulary.getLastReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(userVocabulary.getEf()).thenReturn(10.0d);
        when(userVocabulary.getReviewTimes()).thenReturn(3);
        when(userVocabulary.getNextReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(userVocabulary).setAddTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setEf((Double) any());
        doNothing().when(userVocabulary).setLastReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setNextReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setReviewTimes((Integer) any());
        doNothing().when(userVocabulary).setUserId((String) any());
        doNothing().when(userVocabulary).setWord((String) any());
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(10.0d);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(1);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        superMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(userVocabulary, 3);
        verify(userVocabulary).getEf();
        verify(userVocabulary).getReviewTimes();
        verify(userVocabulary).getLastReviewTime();
        verify(userVocabulary).getNextReviewTime();
        verify(userVocabulary).setAddTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setEf((Double) any());
        verify(userVocabulary).setLastReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setNextReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setReviewTimes((Integer) any());
        verify(userVocabulary).setUserId((String) any());
        verify(userVocabulary).setWord((String) any());
    }

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime6() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.time.Duration.between(Duration.java:473)
        //       at com.ailearningvocabulary.bomnmi.web.service.impl.SuperMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(SuperMemo2SpaceRepetitionServiceImpl.java:42)
        //   In order to prevent calculateEFAndNextReviewTime(UserVocabulary, Integer)
        //   from throwing NullPointerException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   calculateEFAndNextReviewTime(UserVocabulary, Integer).
        //   See https://diff.blue/R013 to resolve this issue.

        UserVocabulary userVocabulary = new UserVocabulary();
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(2.5);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(3);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        superMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(userVocabulary, 3);
    }

    /**
     * Method under test: {@link SuperMemo2SpaceRepetitionServiceImpl#calculateEFAndNextReviewTime(UserVocabulary, Integer)}
     */
    @Test
    void testCalculateEFAndNextReviewTime7() {
        UserVocabulary userVocabulary = mock(UserVocabulary.class);
        when(userVocabulary.getLastReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(userVocabulary.getEf()).thenReturn(10.0d);
        when(userVocabulary.getReviewTimes()).thenReturn(2);
        when(userVocabulary.getNextReviewTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(userVocabulary).setAddTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setEf((Double) any());
        doNothing().when(userVocabulary).setLastReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setNextReviewTime((LocalDateTime) any());
        doNothing().when(userVocabulary).setReviewTimes((Integer) any());
        doNothing().when(userVocabulary).setUserId((String) any());
        doNothing().when(userVocabulary).setWord((String) any());
        userVocabulary.setAddTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setEf(10.0d);
        userVocabulary.setLastReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setNextReviewTime(LocalDateTime.of(1, 1, 1, 1, 1));
        userVocabulary.setReviewTimes(1);
        userVocabulary.setUserId("42");
        userVocabulary.setWord("Word");
        superMemo2SpaceRepetitionServiceImpl.calculateEFAndNextReviewTime(userVocabulary, 3);
        verify(userVocabulary).getEf();
        verify(userVocabulary).getReviewTimes();
        verify(userVocabulary).getNextReviewTime();
        verify(userVocabulary).setAddTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setEf((Double) any());
        verify(userVocabulary).setLastReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setNextReviewTime((LocalDateTime) any());
        verify(userVocabulary, atLeast(1)).setReviewTimes((Integer) any());
        verify(userVocabulary).setUserId((String) any());
        verify(userVocabulary).setWord((String) any());
    }
}

