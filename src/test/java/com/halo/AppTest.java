package com.halo;

import com.halo.zhttp.BasicHttpUtils;
import com.halo.zhttp.AdvanceHttpUtils;
import com.halo.zhttp.HttpUtilsException;
import com.halo.zhttp.assembler.AssemblyLine;
import com.halo.zhttp.assembler.AuthorizationHeaderAssembler;
import com.halo.zhttp.assembler.AuthorizationType;
import com.halo.zjson.JSONUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        BasicHttpUtils basicHttpUtils = new BasicHttpUtils<>(String.class, "http://192.168.88.201:8080/itc/main/getItApplication");
        String response = null;
        try {
            response = (String) basicHttpUtils.get();
        } catch (HttpUtilsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        assertTrue(true);
    }

    @Test
    public void testBeanGet() {
        AdvanceHttpUtils advanceHttpUtils = new AdvanceHttpUtils<ResponseCodeTableBean, ResponseCodeTableBean>("http://192.168.88.201:8080/itc/main/getItApplication");
        advanceHttpUtils.setResponseJsonUtils(new JSONUtils(ResponseCodeTableBean.class));
        ResponseCodeTableBean codeTableBean = null;
        try {
            codeTableBean = (ResponseCodeTableBean) advanceHttpUtils.get();
        } catch (HttpUtilsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    @Test
    public void testBeanPost() {
        AdvanceHttpUtils advanceHttpUtils = new AdvanceHttpUtils<MedalRecordBean, ResponseCodeTableBean>("http://192.168.88.201:8080/itc/tribe/addMedalRecord");

        AssemblyLine assemblyLine = advanceHttpUtils.getAssemblyLine();
        if (null == assemblyLine) {
            assemblyLine = new AssemblyLine();
            advanceHttpUtils.setAssemblyLine(assemblyLine);
        }
        AuthorizationHeaderAssembler authorizationHeaderAssembler = new AuthorizationHeaderAssembler("admin", "1234", AuthorizationType.Basic);
        assemblyLine.addAssembler(authorizationHeaderAssembler);

        advanceHttpUtils.setRequestJsonUtils(new JSONUtils(MedalRecordBean.class));

        MedalRecordBean medalRecordBean = new MedalRecordBean();
        medalRecordBean.setMedalWinner("苏钰");
        medalRecordBean.setMedalClause("结对共进");
        medalRecordBean.setMedalName("礼仪章");
        medalRecordBean.setIsWinning((short) 1);
        medalRecordBean.setPresenter("xulm");
        advanceHttpUtils.setRequestJsonBean(medalRecordBean);

        advanceHttpUtils.setResponseJsonUtils(new JSONUtils(ResponseCodeTableBean.class));

        ResponseCodeTableBean responseCodeTableBean = null;
        try {
            responseCodeTableBean = (ResponseCodeTableBean) advanceHttpUtils.post();
        } catch (HttpUtilsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }

    @Test
    public void testFileDownload() {
        BasicHttpUtils basicHttpUtils = new BasicHttpUtils<>(File.class, "http://192.168.88.201:8080/itc/main/getItApplication");
        File downloadFile = null;
        try {
            downloadFile = (File) basicHttpUtils.get();
        } catch (HttpUtilsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert downloadFile != null;
        System.out.println(downloadFile.getAbsolutePath());

        assertTrue(true);
    }
}
