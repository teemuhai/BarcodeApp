package com.example.teemurytsola.cameraapptest;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.List;

/**
 * Created by teemurytsola on 14/09/2017.
 */

public class XmlGenerator {
    List messages;

    private String writeXml(List<BarcodeItem> messages){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-16", true);
            serializer.startTag("", "messages");
            serializer.attribute("", "number", String.valueOf(messages.size()));
            for (BarcodeItem msg: messages){
                serializer.startTag("", "message");
                serializer.attribute("", "date", msg.getBarcode());
                serializer.startTag("", "title");
                serializer.text(String.valueOf(msg.getItemCount()));
                serializer.endTag("", "title");
                serializer.startTag("", "url");
                serializer.startTag("", "body");
                serializer.endTag("", "body");
                serializer.endTag("", "message");
            }
            serializer.endTag("", "messages");
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
