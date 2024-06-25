//package com.kshrd.krorya.configuration;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//
//@Component
//public class GoogleTokenVerifier {
//
//    private static final String CLIENT_ID = "YOUR_CLIENT_ID.apps.googleusercontent.com";
//    private static final String GOOGLE_CERTS_URL = "https://www.googleapis.com/oauth2/v3/certs";
//    private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//    private final GoogleIdTokenVerifier verifier;
//
//    public GoogleTokenVerifier() throws GeneralSecurityException, IOException {
//        GooglePublicKeysManager publicKeysManager = new GooglePublicKeysManager.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
//                .setPublicCertsEncodedUrl(GOOGLE_CERTS_URL)
//                .build();
//
//        this.verifier = new GoogleIdTokenVerifier.Builder(publicKeysManager)
//                .setAudience(Collections.singletonList(CLIENT_ID))
//                .build();
//    }
//
//    public GoogleIdToken.Payload verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//        if (idToken != null) {
//            return idToken.getPayload();
//        } else {
//            throw new GeneralSecurityException("Invalid ID token.");
//        }
//    }
//}