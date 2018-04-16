package com.arches.keystone.samples.article;

import com.arches.keystone.model.*;
import com.arches.keystone.samples.util.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Article Tests")
class ArticleTests {

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
    @DisplayName("Get article list")
    void getArticleList() throws Exception {
        List<Article> list = restClient.get("/content/", ArticleList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("Get email article list")
    void getEMailArticleList() throws Exception {
        List<Article> list = restClient.get("/content/email/channel", ArticleList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("Get sms article list")
    void getSMSArticleList() throws Exception {
        List<Article> list = restClient.get("/content/sms/channel", ArticleList.class);
        assertTrue(list.size()>0);
    }

    @Test
    @DisplayName("save web article")
    void saveArticle() throws Exception {

        Article article = new Article();
        article.setTitle("test title");
        article.setChannel("web");
        article.setName("names");
        article.setContent("test web content".getBytes());
        Status status = restClient.post("/content/", article, Status.class);
        assertNotNull(status.getEntityUid());
    }

    @Test
    @DisplayName("save article with subject and preheaders")
    void saveArticleWithSubject() throws Exception {

        Article article = new Article();
        article.setTitle("test email title");
        article.setChannel("email");
        article.setName("names");
        article.setContent("test email content".getBytes());
        ArticleSubject sub1 = new ArticleSubject();
        sub1.setSubject("subject 1");
        ArticleSubject sub2 = new ArticleSubject();
        sub1.setSubject("subject 2");
        ArticleSubject[] subjectList = new ArticleSubject[2];
        subjectList[0] = sub1;
        subjectList[1]=sub2;
        article.setArticleSubjectList(subjectList);

        ArticlePreheader pre1 = new ArticlePreheader();
        sub1.setSubject("preheader 1");
        ArticlePreheader pre2 = new ArticlePreheader();
        sub1.setSubject("preheader 2");
        ArticlePreheader[] preheaderList = new ArticlePreheader[2];
        preheaderList[0] = pre1;
        preheaderList[1]=pre2;
        article.setArticlePreheaderList(preheaderList);

        Status status = restClient.post("/content/", article, Status.class);
        assertNotNull(status.getEntityUid());

        Article dbarticle = restClient.get("/content/"+status.getEntityUid(), Article.class);

        article.setTitle("Updated"+dbarticle.getTitle());
        status = restClient.put("/content/", dbarticle, Status.class);

    }


    @Test
    @DisplayName("save SMS article with messages")
    void saveSMSArticleWithSubject() throws Exception {

        Article article = new Article();
        article.setTitle("test sms title");
        article.setChannel("sms");
        article.setName("names");
        ArticleSMS msg1 = new ArticleSMS();
        msg1.setMessage("message 1");
        ArticleSMS msg2 = new ArticleSMS();
        msg2.setMessage("message 2");
        ArticleSMS[] messageList = new ArticleSMS[2];
        messageList[0] = msg1;
        messageList[1]=msg2;
        article.setArticleSMSList(messageList);

        Status status = restClient.post("/content/", article, Status.class);
        assertNotNull(status.getEntityUid());
    }


    @Test
    @DisplayName("get content by uuid")
    void getContentByUid() throws Exception {

        List<Article> list = restClient.get("/content/", ArticleList.class);
        if(list.size()>0){
            Article article = list.get(list.size()-1);
            String content = restClient.get("/content/"+article.getUuid()+"/content", String.class);
            System.out.println(content);
        }
    }

}

class ArticleList extends ArrayList<Article> {

}


class ProfileAttributeList extends ArrayList<ProfileAttribute> {

}