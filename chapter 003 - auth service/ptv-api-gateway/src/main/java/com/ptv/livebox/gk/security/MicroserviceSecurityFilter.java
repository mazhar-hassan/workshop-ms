package com.ptv.livebox.gk.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;


@Component
public class MicroserviceSecurityFilter implements GlobalFilter {

    @Autowired
    SystemSettings settings;

    final Logger logger =
            LoggerFactory.getLogger(MicroserviceSecurityFilter.class);

    public static KeyPair keyPair;

    public void loadKey() throws IOException, CertificateException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
        if (keyPair == null) {
            File file = ResourceUtils.getFile("classpath:ms-workshop-PKCS-12.p12");
            keyPair = loadFromPKCS12(settings.getKeystoreAlias(), file, settings.getKeystorePassword().toCharArray());
        }
    }

    static KeyPair loadFromPKCS12(String alias, File file, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException {
        KeyStore pkcs12KeyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(file);

        try {
            pkcs12KeyStore.load(fis, password);
        } catch (Throwable var10) {
            try {
                fis.close();
            } catch (Throwable var9) {
                var10.addSuppressed(var9);
            }

            throw var10;
        }

        fis.close();
        KeyStore.PasswordProtection param = new KeyStore.PasswordProtection(password);
        KeyStore.Entry entry = pkcs12KeyStore.getEntry(alias, param);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            throw new KeyStoreException("That's not a private key!");
        } else {
            KeyStore.PrivateKeyEntry privKeyEntry = (KeyStore.PrivateKeyEntry) entry;
            PublicKey publicKey = privKeyEntry.getCertificate().getPublicKey();
            PrivateKey privateKey = privKeyEntry.getPrivateKey();
            return new KeyPair(publicKey, privateKey);
        }
    }


    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {
        logger.info("******************** Global Pre Filter executed ****************************");

        String jwtToken = getToken(exchange.getRequest().getHeaders());

        if (jwtToken == null) {
            throw new SecurityException("Token is missing in request");
        }

        String token = validateToken(jwtToken);
        logger.info("******************** Global Pre Filter executed ****************************");
        return chain.filter(
                exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header("user", token)
                                .build())
                        .build());
        //return chain.filter(exchange);
    }

    private String validateToken(String token) {
        try {
            loadKey();
            String jwtJson = JwtUtils.extractJSON(token, keyPair.getPublic());
            System.out.println(jwtJson);
            return jwtJson;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getToken(HttpHeaders headers) {
        if (headers.containsKey("Authorization")) {
            if (null != headers.get("Authorization").get(0)) {
                return headers.get("Authorization").get(0).replace("Bearer ", "");
            }
        }

        return null;
    }
}