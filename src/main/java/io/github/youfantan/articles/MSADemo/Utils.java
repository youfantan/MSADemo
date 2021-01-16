package io.github.youfantan.articles.MSADemo;


import java.net.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefMessageRouterHandler;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

public class Utils extends JFrame {
    public static final String MicrosoftOAuthAuthorizeUrl="https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL &redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf";
    public static final String MicrosoftOAuthDesktopUrl="https://login.live.com/oauth20_token.srf";
    public static final String XBLUrl="https://user.auth.xboxlive.com/user/authenticate";
    public static final String XSTSUrl="https://xsts.auth.xboxlive.com/xsts/authorize";
    public static String res=null;

    public static void main(String[] args) throws IOException{
        Utils utils=new Utils();
        utils.CreateFrame();
    }
    public String GetXSTSToken(String token) throws IOException {
        URL ConnectUrl=new URL(XSTSUrl);
        String param=null;
        List<String> tokens=new ArrayList<>();
        tokens.add(token);
        JSONObject xbl_param=new JSONObject(true);
        JSONObject xbl_properties=new JSONObject(true);
        xbl_properties.put("SandboxId","RETAIL");
        xbl_properties.put("UserTokens",JSONArray.parse(JSON.toJSONString(tokens)));
        xbl_param.put("Properties",xbl_properties);
        xbl_param.put("RelyingParty","rp://api.minecraftservices.com/");
        xbl_param.put("TokenType","JWT");
        param=JSON.toJSONString(xbl_param);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response=new StringBuilder();
        String Buffer=null;
        while ((Buffer=reader.readLine())!=null){
            response.append(Buffer);
        }
        JSONObject response_obj=JSON.parseObject(response.toString());
        return response_obj.getString("Token");
    }
    public String GetXBLToken(String token) throws IOException {
        URL ConnectUrl=new URL(XBLUrl);
        String param=null;
        JSONObject xbl_param=new JSONObject(true);
        JSONObject xbl_properties=new JSONObject(true);
        xbl_properties.put("AuthMethod","RPS");
        xbl_properties.put("SiteName","user.auth.xboxlive.com");
        xbl_properties.put("RpsTicket",token);
        xbl_param.put("Properties",xbl_properties);
        xbl_param.put("RelyingParty","http://auth.xboxlive.com");
        xbl_param.put("TokenType","JWT");
        param=JSON.toJSONString(xbl_param);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        int flag=0;
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response=new StringBuilder();
        String Buffer=null;
        while ((Buffer=reader.readLine())!=null){
            response.append(Buffer);
        }
        JSONObject response_obj=JSON.parseObject(response.toString());
        return response_obj.getString("Token");
    }
    public String RefreshToken(String token) throws IOException {
        URL ConnectUrl=new URL(MicrosoftOAuthDesktopUrl);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        String param="client_id=00000000402b5328" +
                "&refresh_token="+token+
                "&grant_type=refresh_token" +
                "&redirect_uri=https://login.live.com/oauth20_desktop.srf" +
                "&scope=service::user.auth.xboxlive.com::MBI_SSL";
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response=new StringBuilder();
        String Buffer=null;
        while ((Buffer=reader.readLine())!=null){
            response.append(Buffer);
        }
        JSONObject response_obj=JSON.parseObject(response.toString());
        return response_obj.getString("access_token");
    }
    public String GetMSAToken(String code) throws IOException {
        URL ConnectUrl=new URL(MicrosoftOAuthDesktopUrl);
        HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        String param="client_id=00000000402b5328" +
                "&code=" +code+
                "&grant_type=authorization_code" +
                "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf" +
                "&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL";
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        BufferedWriter wrt=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        wrt.write(param);
        wrt.flush();
        wrt.close();
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response=new StringBuilder();
        String Buffer=null;
        while ((Buffer=reader.readLine())!=null){
            response.append(Buffer);
        }
        JSONObject response_obj=JSON.parseObject(response.toString());
        return response_obj.getString("access_token");
    }
    public boolean CreateFrame(){
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED){
                    dispose();
                }
            }
        });
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = false;
        CefApp cefApp=CefApp.getInstance(settings);
        CefClient cefClient = cefApp.createClient();
        CreateJSRouter(cefClient);
        CefBrowser cefBrowser = cefClient.createBrowser(MicrosoftOAuthAuthorizeUrl, false, false);
        getContentPane().add(cefBrowser.getUIComponent(), BorderLayout.CENTER);
        pack();
        setTitle("Test For MSA");
        setSize(1260, 720);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
        cefClient.addDisplayHandler(new CefDisplayHandler() {
            @Override
            public void onAddressChange(CefBrowser cefBrowser, CefFrame cefFrame, String s) {
                if (s.contains("https://login.live.com/oauth20_desktop.srf?code=")){
                    String code=s.substring(s.indexOf("=")+1);
                    cefBrowser.loadURL(System.getProperty("user.dir")+"/OnGotToken.html");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Utils utils=new Utils();
                            String MSAToken=null;
                            String XBLToken=null;
                            String XSTSToken=null;
                            try {
                                MSAToken=utils.GetMSAToken(code);
                                XBLToken=utils.GetXBLToken(MSAToken);
                                XSTSToken=utils.GetXSTSToken(XBLToken);
                                res=XSTSToken;
                                System.out.println("[XSTS Token]"+XSTSToken);
                                cefBrowser.loadURL(System.getProperty("user.dir")+"/Result.html");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }

            @Override
            public void onTitleChange(CefBrowser cefBrowser, String s) {
            }

            @Override
            public boolean onTooltip(CefBrowser cefBrowser, String s) {
                return false;
            }

            @Override
            public void onStatusMessage(CefBrowser cefBrowser, String s) {

            }

            @Override
            public boolean onConsoleMessage(CefBrowser cefBrowser, CefSettings.LogSeverity logSeverity, String s, String s1, int i) {
                return false;
            }
        });
        return false;
    }
    public void CreateJSRouter(CefClient client) {
        CefMessageRouter.CefMessageRouterConfig cmrc=new CefMessageRouter.CefMessageRouterConfig("jsact","jsactCancel");
        CefMessageRouter cmr=CefMessageRouter.create(cmrc);
        cmr.addHandler(new CefMessageRouterHandler() {
            @Override
            public void setNativeRef(String str, long val) {
            }

            @Override
            public long getNativeRef(String str) {
                return 0;
            }

            @Override
            public void onQueryCanceled(CefBrowser browser, CefFrame frame, long query_id) {
            }

            @Override
            public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request, boolean persistent,
                                   CefQueryCallback callback) {
                System.out.println("request:"+request+"\nquery_id:"+query_id+"\npersistent:"+persistent);
                callback.success(res);
                return true;
            }
        }, true);
        client.addMessageRouter(cmr);
    }
}
