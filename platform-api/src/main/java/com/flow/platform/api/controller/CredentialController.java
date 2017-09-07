/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flow.platform.api.controller;

import com.flow.platform.api.domain.credential.Credential;
import com.flow.platform.api.domain.credential.CredentialType;
import com.flow.platform.api.domain.credential.RSAKeyPair;
import com.flow.platform.api.service.CredentialService;
import com.flow.platform.domain.Jsonable;
import com.flow.platform.util.Logger;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lhl
 */

@RestController
@RequestMapping(path = "/credentials")
public class CredentialController {

    private final static Logger LOGGER = new Logger(CredentialController.class);

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private Path workspace;

    /**
     * @api {get} /credentials List
     * @apiGroup Credenital
     * @apiDescription List credentials
     *
     * @apiSuccessExample {json} Success-Response
     *  [
            {
                "name": "android-credential",
                "type": "ANDROID",
                "detail": {
                    "file": {
                        "name": "android.jks"
                    },
                    "keyStorePassword": "12345",
                    "keyStoreAlias": "android",
                    "keyStoreAliasPassword": "12345"
                },
                "createdAt": 1504737923,
                "updatedAt": 1504737923
            },

            {
                "name": "ios-credential",
                "type": "IOS",
                "detail": {
                    "provisionProfiles": [
                        {
                            "name": "pp"
                        }
                    ],

                    "p12s": [
                        {
                            "password": "12345",
                            "name": "p12"
                        }
                    ]
                },
                "createdAt": 1504737923,
                "updatedAt": 1504737923
            },

            {
                "name": "ras-credential",
                "type": "RSA",
                "detail": {
                    "publicKey": "public key",
                    "privateKey": "private key"
                },
                "createdAt": 1504737923,
                "updatedAt": 1504737923
            },

            {
                "name": "username-credential",
                "type": "USERNAME",
                "detail": {
                    "username": "user",
                    "password": "pass"
                },
                "createdAt": 1504737923,
                "updatedAt": 1504737923
            }
        ]
     */
    @GetMapping
    public List<Credential> list(@RequestParam(required = false) String types) {
        final Set<CredentialType> typeSet = new HashSet<>(CredentialType.values().length);

        if (!Strings.isNullOrEmpty(types)) {
            types = types.trim();

            ControllerUtil.extractParam(types, input -> {
                typeSet.add(CredentialType.valueOf(input.toUpperCase()));
                return null;
            });
        }

        return credentialService.list(typeSet);
    }

    /**
     * @api {get} /credentials Show
     * @apiGroup Credenital
     * @apiDescription List credentials
     *
     * @apiSuccessExample {json} Success-Response
     *
     *  reference on List item
     */
    @GetMapping(path = "/{name}")
    public String show(@PathVariable String name) {
        Credential credential = credentialService.find(name);
        return credential.toJson();
    }

    /**
     * @api {post} /credentials Create
     * @apiParamExample {json} Request-Body:
     *
     *  reference on List item
     * @apiGroup Credenital
     * @apiDescription Create credentials
     *
     * @apiSuccessExample {json} Success-Response
     *
     *  reference on List item
     */
    @PostMapping
    public Object create(@RequestBody String credentialJson) {
//        Credential credential = Jsonable.GSON_CONFIG.fromJson(credentialJson, Credential.class);
//        Object o = Jsonable.GSON_CONFIG.fromJson(credentialJson, credential.getCredentialType().getClazz());
//        return credentialService.create((Credential) o);

        return null;
    }

    /**
     * @api {delete} /credentials Delete
     * @apiParam {String} name Credential name
     * @apiGroup Credenital
     * @apiDescription Delete credential
     */
    @DeleteMapping(path = "/{name}")
    public void delete(@PathVariable String name) {
        credentialService.delete(name);
    }

    /**
     * @api {get} /credentials/ssh/key Gen Rsa Key Pari
     * @apiGroup Credenital
     * @apiDescription Generate RSA key pair
     *
     * @apiSuccessExample {json} Success-Response
     *  {
     *      privateKey: xxx,
     *      publicKey: xxx
     *  }
     */
    @GetMapping(path = "/ssh/keys")
    public RSAKeyPair getKeys() {
        return credentialService.generateRsaKey();
    }
//
//    /**
//     * @api {Post} /credentials/fileUpload
//     * @apiName uploadFile
//     * @apiGroup Credential
//     * @apiDescription upload files
//     *
//     * @apiSuccessExample {json} Success-Response:
//     *
//     *       {
//     *         "/aa/a/aa/a"
//     *       }
//     */
//    @PostMapping("/fileUpload")
//    public List<String> filesUpload(MultipartFile[] files) {
//        List<String> list = new ArrayList<>();
//        if (files != null && files.length > 0) {
//            for (int i = 0; i < files.length; i++) {
//                MultipartFile file = files[i];
//                list.add(saveFile(file));
//            }
//            return list;
//        }
//        LOGGER.trace("upload files failure");
//        return null;
//    }
//
//    private String saveFile(MultipartFile file) {
//        if (!file.isEmpty()) {
//            try {
//                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//                int length = getAllowSuffix().indexOf(suffix);
//                if (length == -1) {
//                    throw new IllegalArgumentException("Please upload allowed file format");
//                }
//                String fileName = getFileNameNew() + "_" + file.getOriginalFilename();
//                file.transferTo(Paths.get(workspace.toString(), "uploads/", fileName).toFile());
//                return Paths.get(workspace.toString()).toString() + "/uploads/" + fileName;
//            } catch (Exception e) {
//                LOGGER.trace("upload files failure");
//            }
//        }
//
//        return "save file failure";
//    }
//
//    private String getFileNameNew() {
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        return fmt.format(new Date());
//    }
}
