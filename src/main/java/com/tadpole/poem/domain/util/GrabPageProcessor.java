package com.tadpole.poem.domain.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.tadpole.poem.domain.Job;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by jerryjiang on 22/4/2016.
 */
public class GrabPageProcessor {

    public static WebClient webClient = null;

    static {

        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(5000);

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);


    }

    public static final void grabPoemCaseOneOneZero(Job job) {
        try {
            URL url = new URL(job.getTarget());
            final HtmlPage singleDataPage = webClient.getPage(url);

            List<?> poemList = singleDataPage.getByXPath("/html/body/div[4]/div[1]/div[2]/p[5]");

            Object firstElementInPoem = poemList.get(0);
            if (firstElementInPoem != null && firstElementInPoem instanceof HtmlParagraph) {

                HtmlParagraph htmlParagraph = (HtmlParagraph) firstElementInPoem;

                System.out.println(htmlParagraph.asText());
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        //http://so.gushiwen.org/type.aspx?p=1
        Job job = new Job();
        job.setTarget("http://so.gushiwen.org/view_49386.aspx");

        grabPoemCaseOneOneZero(job);
    }
}
