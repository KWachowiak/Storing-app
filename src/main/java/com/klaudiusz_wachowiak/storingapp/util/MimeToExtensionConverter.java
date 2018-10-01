package com.klaudiusz_wachowiak.storingapp.util;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

@Component
public class MimeToExtensionConverter {

    public String convertToExtension(String contentType) throws MimeTypeException {

        if (contentType.contains("html")) {
            return ".html";
        }

        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType contentExtension = allTypes.forName(contentType);

        return contentExtension.getExtension();
    }
}
