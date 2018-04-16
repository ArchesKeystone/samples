package com.arches.keystone.samples.questions;

import com.arches.keystone.model.*;
import com.arches.keystone.samples.util.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SurveyTests {


    static String userName;
    static String password;
    static String keystoneUrl;
    static RestClient restClient;

    @BeforeAll
    public static void init() throws Exception {
        Properties props = new Properties();

        InputStream is = ClassLoader.getSystemResourceAsStream("application.properties");
        props.load(is);
        userName = (String) props.get("keystone.userName");
        password = (String) props.get("keystone.password");
        keystoneUrl = (String) props.get("keystone.url");
        restClient = new RestClient(keystoneUrl, userName, password);
    }

    @Test
    @DisplayName("Get survey list")
    void getSurveys() throws Exception {
        List<Questionnaire> list = restClient.get("/survey/", QuestionnaireList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("Add survey")
    void addSurvey() throws Exception {
        Questionnaire survey = new Questionnaire();
        survey.setName("test survey");
        Status status = restClient.post("/survey/", survey, Status.class);
        assertNotNull(status.getEntityUid());
        survey.setUuid(status.getEntityUid());

        survey.setName("updated test survey");
        status = restClient.put("/survey/", survey, Status.class);
        assertNotNull(status.getEntityUid());
    }


    @Test
    @DisplayName("Get question list")
    void getQuestions() throws Exception {
        List<Question> list = restClient.get("/quest/", QuestionList.class);
        assertTrue(list.size()>0);
    }


    @Test
    @DisplayName("question operations")
    void addQuestion() throws Exception {
        Question question = new Question();
        question.setQuestion("test question");
        question.setResponseType(1);

        List<QuestionOption> options = new LinkedList<QuestionOption>();
        QuestionOption option = new QuestionOption();
        option.setOptionText("option 1");
        options.add(option);

        option = new QuestionOption();
        option.setOptionText("option 2");
        options.add(option);

        question.setOptions(options);
        Status status = restClient.post("/quest/", question, Status.class);
        assertNotNull(status.getEntityUid());

        question = restClient.get("/quest/"+status.getEntityUid(), Question.class);

        question.setQuestion("updated test question");

        status = restClient.put("/quest/", question, Status.class);

    }


    @Test
    @DisplayName("Enable/disable question")
    void enableQuestion() throws Exception {
        QuestionList list = restClient.get("/quest/", QuestionList.class);
        assertTrue(list.size()>0);

        Question question = list.get(0);
        Status status = restClient.delete("/quest/"+question.getUuid(), Status.class);

        status = restClient.put("/quest/"+question.getUuid()+"/activate", null, Status.class);



    }

    @Test
    @DisplayName("Get enrollment form questions")
    void getEnrollmentQuestions() throws Exception {
        QuestionList list = restClient.get("/survey/enrollQuestions", QuestionList.class);
        assertTrue(list.size()>0);
    }


}

class QuestionnaireList extends ArrayList<Questionnaire> {

}

class QuestionList extends ArrayList<Question> {

}

class ProfileAttributeList extends ArrayList<ProfileAttribute> {

}