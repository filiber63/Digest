package com.digest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DigestController {
    @FXML
    private TextField systemId;

    @FXML
    private TextField authRequestId;

    @FXML
    private TextField requestDate;

    @FXML
    private TextField pwdHash;

    @FXML
    private TextField digest;

    @FXML
    private CheckBox autoCreate;

    @FXML
    private Button btnClearSystemId;

    @FXML
    private Button btnClearAuthRequestId;

    @FXML
    private Button btnClearRequestDate;

    @FXML
    private Button btnClearAuthToken;

    @FXML
    private TextArea requestXMLCode;



    //*************************** ОТРАБОТКА НАЖАТИЯ КНОПКИ ФОРМИРОВАНИЯ DIGEST ****************************************
    public void onGetDigestButtonClick() throws NoSuchAlgorithmException, IOException {
        String authParams;
        //------------------------------------- Чтение файла ----------------------------------------------------------
        File file = new File("E:/IdeaProjects/Digest/out/artifacts/Digest_jar/Digest.ini");
//        File file = new File("Digest.ini");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);

        //--------------- Автоматическое формирование requestDate и authRequestId -------------------------------------
        if (autoCreate.isSelected()) {
            requestDate.setText(getRequestDate());
            authRequestId.setText(getAuthRequestId());
            systemId.setText(reader.readLine());
            pwdHash.setText(reader.readLine());
        }
        //---------------------------- Формирование Digest ------------------------------------------------------------
        authParams = systemId.getText()+":"+authRequestId.getText()+":"+requestDate.getText()+":"+pwdHash.getText();
        digest.setText(getDigest(authParams));
        //--------------------------- Формирование XML-кода запроса ---------------------------------------------------
        requestXMLCode.setText(getXMLCode(systemId.getText(), authRequestId.getText(), requestDate.getText(), digest.getText() ));
    }

    //********************************** ОЧИСТКА ВСЕХ ПОЛЕЙ ***********************************************************
    public void onClearButtonClick() {
        systemId.setText("");
        authRequestId.setText("");
        requestDate.setText("");
        pwdHash.setText("");
    }

    //******************************* ОЧИСТКА ВЫБРАННОГО ПОЛЯ *********************************************************
    public void onClearFieldButtonClick(ActionEvent actionEvent){
        if (actionEvent.getSource()==btnClearSystemId) systemId.setText(""); else
        if (actionEvent.getSource()==btnClearAuthRequestId) authRequestId.setText(""); else
        if (actionEvent.getSource()==btnClearRequestDate) requestDate.setText(""); else
        if (actionEvent.getSource()==btnClearAuthToken) pwdHash.setText("");
    }

    //********************** ПРОЦЕДУРА ПОЛУЧЕНИЯ МОМЕНТА ФОРМИРОВАНИЯ ЗАПРОСА ДЛЯ requestDate *************************
    private String getRequestDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).replace(' ','T');
    }

    //********************* ПРОЦЕДУРА ФОРМИРОВАНИЯ UUID ДЛЯ authRequestId *********************************************
    private String getAuthRequestId(){
        UUID uuid = UUID. randomUUID();
        return uuid. toString();
    }

    //********************** ПРОЦЕДУРА ФОРМИРОВАНИЯ XML-КОДА ЗАПРОСА СОЗДАНИЯ СЕССИИ **********************************
    private String getXMLCode(String systemId, String authRequestId, String requestDate, String digest){
        return String.format("""
                <authParams>
                       <systemId>%s</systemId>
                       <authRequestId>%s</authRequestId>
                       <requestDate>%s</requestDate>
                       <digest>%s</digest>
                </authParams>""", systemId, authRequestId, requestDate, digest);
    }

    //**************** ПРОЦЕДУРЫ ВЫЧИСЛЕНИЯ ХЕША по АЛГОРИТМУ SHA256, ПОЛУЧЕННЫЕ ОТ РАЗРАБОТЧИКА **********************
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    private String getDigest(String authParams) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                authParams.getBytes(StandardCharsets.UTF_8));

        return   bytesToHex(encodedhash);//hash(digestStr,"SHA-256");//new String(Base64.encode(encodedhash));//Base64.toBase64String(encodedhash);//bytesToHex(encodedhash);

    }

}