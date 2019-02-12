var hooks = require('hooks');
var before = hooks.before;
var after = hooks.after;

var responseStash = {};


// call the addVDC with the blueprint
before("/AddVDC > POST > 200", function (transaction) {
	transaction.request.headers['ConcreteBlueprint'] = "{   \"INTERNAL_STRUCTURE\":{        \"Overview\":{           \"name\":\"1\",         \"description\":\"This VDC provides information about the weather in Athens, Greece\",         \"tags\":           [             {                \"method_id\": \"getAllBloodTest\",                \"tags\":[\"t1\", \"t2\"]             }           ]      },      \"Data_Sources\":[           {              \"id\":\"MinioDS1\",           \"class\":\"relational database\",            \"type\":\"sql\",            \"parameters\":{                 \"hostname\":\"localhost\",               \"port\":\"8000\",               \"proxy-hostname\":\"localhost\",               \"proxy-port\":\"9800\",               \"username\":\"user1\",               \"password\":\"pass1\"            },            \"location\" : \"edge\",\t         \"resourceUsed\":  \t         \t[\t\t            {  \t\t               \"type\":\"space\",\t\t               \"total\":\"40\",\t\t               \"unit\":\"GB\"\t\t            },\t\t            {  \t\t               \"type\":\"memory\",\t\t               \"total\":\"3\",\t\t               \"unit\":\"GB\"\t\t            }\t           \t]          },         {              \"id\":\"MinioDS2\",            \"type\":\"sql\",\t\t\t\"class\":\"relational database\",            \"parameters\":{                 \"hostname\":\"localhost\",               \"port\":\"8000\",               \"proxy-hostname\":\"localhost\",               \"proxy-port\":\"9800\",               \"username\":\"user1\",               \"password\":\"pass1\"                           },            \"location\" : \"edge\",\t         \"resourceUsed\":  \t         \t[\t\t            {  \t\t               \"type\":\"space\",\t\t               \"total\":\"20\",\t\t               \"unit\":\"GB\"\t\t            },\t\t            {  \t\t               \"type\":\"memory\",\t\t               \"total\":\"5\",\t\t               \"unit\":\"GB\"\t\t            }\t           \t]           }       ],      \"resourcesAvailable\":[           {              \"name\":\"rescource1\",            \"type\":\"sql\",            \"location\":\"edge\",            \"characteristics\":[\t            {  \t               \"type\":\"space\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            },\t            {  \t               \"type\":\"memory\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            }            ]         },         {              \"name\":\"rescource2\",            \"type\":\"sql\",            \"location\":\"edge\",            \"characteristics\":[\t            {  \t               \"type\":\"space\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            },\t            {  \t               \"type\":\"memory\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            }            ]         },         {              \"name\":\"rescource3\",            \"type\":\"sql\",            \"location\":\"cloud\",            \"characteristics\":[\t            {  \t               \"type\":\"space\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            },\t            {  \t               \"type\":\"memory\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            }            ]         },         {              \"name\":\"rescource4\",            \"type\":\"computation\",            \"location\":\"edge\",            \"characteristics\":[\t            {  \t               \"type\":\"memory\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"GB\"\t            },\t            {  \t               \"type\":\"CPU\",\t               \"total\":\"100\",\t               \"free\":\"50\",\t               \"unit\":\"percentage\"\t            }            ]         }      ],      \"Flow\":{           \"platform\":\"Node-RED\",         \"source_code\":[              {                 \"id\":\"266d1e0.2f202e2\",               \"type\":\"http in\",               \"z\":\"edca8e77.6ea5e\",               \"name\":\"[http rest web servive in]   CAF\",               \"url\":\"/CAF\",               \"method\":\"post\",               \"upload\":false,               \"swaggerDoc\":\"\",               \"x\":147.78973388671875,               \"y\":194.3920373916626,               \"wires\":[                    [                       \"e78e4849.721ba8\"                  ]               ]            },            {                 \"id\":\"a0810829.8d5538\",               \"type\":\"http response\",               \"z\":\"edca8e77.6ea5e\",               \"name\":\"[http rest web servive out]   CAF\",               \"statusCode\":\"\",               \"headers\":{                 },               \"x\":686.7812347412109,               \"y\":302.5482864379883,               \"wires\":[                 ]            },            {                 \"id\":\"218f7e99.a6fc42\",               \"type\":\"wunderground\",               \"z\":\"edca8e77.6ea5e\",               \"name\":\"\",               \"lon\":\"\",               \"lat\":\"\",               \"city\":\"Athens\",               \"country\":\"Greece\",               \"x\":404.79258728027344,               \"y\":332.3210220336914,               \"wires\":[                    [                       \"a0810829.8d5538\"                  ]               ]            },            {                 \"id\":\"e78e4849.721ba8\",               \"type\":\"switch\",               \"z\":\"edca8e77.6ea5e\",               \"name\":\"[switch]   select VDC exposed method\",               \"property\":\"payload.method_name\",               \"propertyType\":\"msg\",               \"rules\":[                    {                       \"t\":\"eq\",                     \"v\":\"get_weather\",                     \"vt\":\"str\"                  }               ],               \"checkall\":\"true\",               \"outputs\":1,               \"x\":485.7925796508789,               \"y\":156.26419639587402,               \"wires\":[                    [                       \"218f7e99.a6fc42\"                  ]               ]            }         ]      }   },\"DATA_MANAGEMENT\":    [\t\t{\t\t\t\"method_id\":\"GetAllBloodTests\",\t\t\t\"attributes\":            {                \"dataUtility\":                 [                    {                        \"id\": \"availability_9599\",                        \"description\":\"Availability 95-99\",                        \"type\":\"Availability\",\t\t\t\t\t\t\"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"availability\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\": \"percentage\",\t\t\t\t\t\t\t\t\"minimum\":95,\t\t\t\t\t\t\t\t\"maximum\":99\t\t\t\t\t\t\t}\t\t\t\t\t\t\t\t\t\t}\t\t\t\t\t\t                    },                    {                        \"id\": \"responseTime_1\",                        \"description\": \"ResponseTime 1\",                        \"type\": \"ResponseTime\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"responseTime\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"maximum\":1,\t\t\t\t\t\t\t\t\"unit\": \"second\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"volume_10000\",                        \"description\": \"volume 10000\",                        \"type\": \"Volume\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"volume\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"value\":\"10000\",\t\t\t\t\t\t\t\t\"unit\": \"tuple\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}\t\t\t\t\t\t                    },                    {                        \"id\": \"timeliness_06\",                        \"description\": \"Timeliness 0.6\",                        \"type\": \"Timeliness\",\t\t\t\t\t\t\"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"timeliness\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"maximum\":0.6,\t\t\t\t\t\t\t\t\"unit\": \"NONE\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"processCompleteness_90\",                        \"description\": \"Process completeness 90\",                        \"type\": \"Process completeness\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"completeness\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"minimum\":90,\t\t\t\t\t\t\t\t\"unit\": \"percentage\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                           \"id\": \"scaleUpMemory\",                        \"description\":\"scale-up memory\",                        \"type\":\"Scale-up\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"ramGain\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\": \"percentage\",\t\t\t\t\t\t\t\t\"value\": \"150\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"ramLimit\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\": \"percentage\",\t\t\t\t\t\t\t\t\"value\": \"95\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"scaleUpSpace\",                        \"description\":\"scale-up space\",                        \"type\":\"Scale-up\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"spaceGain\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\": \"percentage\",\t\t\t\t\t\t\t\t\"value\": \"110\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"spaceLimit\":     \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\": \"percentage\",\t\t\t\t\t\t\t\t\"value\": \"90\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"throughput_5\",                        \"description\": \"Throughput\",                        \"type\": \"Throughput\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"throughput\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"maximum\":5,\t\t\t\t\t\t\t\t\"unit\": \"Gb/s\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"precision_08\",                        \"description\": \"Precision\",                        \"type\": \"Precision\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"precision\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"minimum\":0.8,\t\t\t\t\t\t\t\t\"unit\": \"none\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"recall_09\",                        \"description\": \"Recall\",                        \"type\": \"Recall\",\t\t\t\t\t\t\"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"recall\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"minimum\":0.9,\t\t\t\t\t\t\t\t\"unit\": \"none\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },                    {                        \"id\": \"accuracy_09\",                        \"description\": \"Accuracy\",                        \"type\": \"Accuracy\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"accuracy\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"minimum\":0.9,\t\t\t\t\t\t\t\t\"unit\": \"none\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    }                ],                \"security\":                [                    {                        \"id\": \"encryptionAES_128\",                        \"description\": \"Encryption AES 128\",                        \"type\": \"Encryption\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"algorithm\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\":\"enum\",\t\t\t\t\t\t\t\t\"value\":\"AES\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"keyLength\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\":\"number\",\t\t\t\t\t\t\t\t\"minimum\":128\t\t\t\t\t\t\t}\t\t\t\t\t\t}                    },\t\t\t\t\t{                        \"id\": \"tracing_99100\",                        \"description\": \"Tracing\",                        \"type\": \"Tracing\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"level\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"datasource\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"sampleRate\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"percentage\",\t\t\t\t\t\t\t   \"minimum\":99,\t\t\t\t\t\t\t   \"maximum\":100\t\t\t\t\t\t\t}  \t\t\t\t\t\t}\t\t\t\t\t},\t\t\t\t\t{                        \"id\": \"aclRoleBasedReadOnly\",                        \"description\": \"ACL rolebased readOnly\",                        \"type\": \"ACL\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"kind\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"rolebased\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"role\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"readOnly\"\t\t\t\t\t\t\t}\t\t\t\t\t\t}\t\t\t\t\t},                    {                        \"id\": \"encryptionAES_256\",                        \"description\": \"Encryption AES 256\",                        \"type\": \"Encryption\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"algorithm\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\":\"enum\",\t\t\t\t\t\t\t\t\"value\":\"AES\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"keyLength\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t\t\"unit\":\"number\",\t\t\t\t\t\t\t\t\"minimum\":256\t\t\t\t\t\t\t}  \t\t\t\t\t\t}                    }               ],                \"privacy\":                [\t\t\t\t\t{\t\t\t\t\t    \"id\": \"purposeControlNCGovernment\",\t\t\t\t\t    \"description\": \"PurposeControl NonCommercial Government\",\t\t\t\t\t    \"type\": \"PurposeControl\",\t\t\t\t\t    \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"allowedPurpose\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"NonCommercial\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"allowedGuarantor\": \t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"Government\"\t\t\t\t\t\t\t} \t\t\t\t\t\t}\t\t\t\t\t},                     {                        \"id\": \"mutationControlNCGovernment\",                        \"description\": \"MutationControl NonCommercial Government\",                        \"type\": \"MutationControl\",                        \"properties\": \t\t\t\t\t\t{\t\t\t\t\t\t\t\"allowedPurpose\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"NonCommercial\"\t\t\t\t\t\t\t},\t\t\t\t\t\t\t\"allowedGuarantor\":\t\t\t\t\t\t\t{\t\t\t\t\t\t\t   \"unit\":\"enum\",\t\t\t\t\t\t\t   \"value\":\"Government\"\t\t\t\t\t\t\t}  \t\t\t\t\t\t}\t\t\t\t\t}\t\t\t\t]            }\t\t}\t],\"ABSTRACT_PROPERTIES\":\t[\t\t{\t\t\t\"method_id\":\"GetAllBloodTests\",                \"goalTrees\":                {                    \"dataUtility\":                    {                        \"type\":\"AND\",                        \"children\":                        [                            {                                \"type\":\"AND\",                                \"children\":[                                    {                                        \"type\":\"OR\",                                        \"leaves\":                                        [                                            {                                                \"id\": \"serviceAvailable\",                                                \"description\": \"Service available\",                                                 \"weight\":1,                                                \"attributes\":                                                 [                                                    \"availability_9599\"                                                ]                                            }                                            ,                                            {                                                \"id\": \"serviceScalable\",                                                \"description\": \"Service Scalable\",                                                \"weight\":1,                                                \"attributes\":                                                [                                                    \"scaleUpMemory\",\"scaleUpSpace\"                                                ]                                                            }                                        ]                                    },                                    {                                        \"type\":\"OR\",                                        \"leaves\":                                        [                                            {                                                \"id\": \"fastDataProcess\",                                                \"description\": \"Fast data process\",                                                 \"weight\":1,                                                \"attributes\":                                                 [                                                    \"responseTime_1\"                                                ]                                            }                                            ,                                            {                                                \"id\": \"fastDataStreaming\",                                                \"description\": \"Fast data streaming\",                                                \"weight\":1,                                                \"attributes\":                                                [                                                    \"throughput_5\"                                                ]                                                            }                                        ]                                    }                                ]                            },                            {                                \"type\":\"AND\",                                \"leaves\":                                [                                    {                                        \"id\": \"dataVolume\",                                        \"description\": \"Data volume\",                                        \"weight\":1,                                        \"attributes\":                                        [                                            \"volume_10000\"                                        ]                                    }                                    ,                                    {                                        \"id\": \"timeliness\",                                        \"description\": \"Timeliness\",                                        \"weight\":1,                                        \"attributes\":                                        [                                            \"timeliness_06\"                                        ]                                                    }                                    ,                                    {                                        \"id\": \"accuracy\",                                        \"description\": \"Accuracy\",                                        \"weight\":1,                                        \"attributes\":                                        [                                            \"accuracy_09\"                                        ]                                                    }                                    ,                                    {                                        \"id\": \"completeness\",                                        \"description\": \"Completeness\",                                        \"weight\":1,                                        \"attributes\":                                        [                                            \"processCompleteness_90\"                                        ]                                                    }                                    ,                                    {                                        \"id\": \"consistency\",                                        \"description\": \"consistency\",                                        \"weight\":1,                                        \"attributes\":                                        [                                            \"precision_08\",\"recall_09\"                                        ]                                                    }                                ]                            }                        ]                    }\t\t\t\t\t,                    \"security\":                    {                        \"type\":\"AND\",                        \"children\":                        [                            {                                \"type\":\"AND\",                                \"leaves\":                                [                                    {                                       \"id\": \"storageEncryption\",                                       \"description\": \"Storage encryption\",                                       \"weight\":1,                                       \"attributes\":[                                            \"encryptionAES_128\"                                       ]                                   }                                    ,                                    {                                       \"id\": \"11\",                                       \"description\": \"Data encryption\",                                       \"weight\":1,                                       \"attributes\":                                       [                                           \"encryptionAES_256\"                                         ]                                   }                                ]                            }                        ],                        \"leaves\":[                           {\t\t\t\t\t\t\t\t\"id\": \"accessControl\",\t\t\t\t\t\t\t\t\"description\": \"Access control\",\t\t\t\t\t\t\t\t\"weight\":1,\t\t\t\t\t\t\t\t\"attributes\":\t\t\t\t\t\t\t\t[\t\t\t\t\t\t\t\t\t\"aclRoleBasedReadOnly\" \t\t\t\t\t\t\t\t]                           }                            ,                           {\t\t\t\t\t\t\t\t\"id\": \"tracing\",\t\t\t\t\t\t\t\t\"description\": \"Tracing and monitoring\",\t\t\t\t\t\t\t\t\"weight\":1,                                \"attributes\":\t\t\t\t\t\t\t\t[\t\t\t\t\t\t\t\t\t\"tracing_99100\" \t\t\t\t\t\t\t\t]                           }                        ]                    }\t\t\t\t\t,\t\t\t\t\t\"privacy\":                    {                        \"type\":\"AND\",                        \"leaves\":                        [                           {\t\t\t\t\t\t\t\t\"id\": \"purposeControl\",\t\t\t\t\t\t\t\t\"description\": \"Purpose control\",\t\t\t\t\t\t\t\t\"weight\":1,                                \"attributes\":[\t\t\t\t\t\t\t\t\t\"purposeControlNCGovernment\" \t\t\t\t\t\t\t\t]                           },                           {\t\t\t\t\t\t\t\t\"id\": \"mutationControl\",\t\t\t\t\t\t\t\t\"description\": \"Mutation control\",\t\t\t\t\t\t\t\t\"weight\":1,                                \"attributes\":[\t\t\t\t\t\t\t\t\t\"mutationControlNCGovernment\" \t\t\t\t\t\t\t\t]                           }                        ]                    }                }            \t\t}\t], \"COOKBOOK_APPENDIX\":{     },\"EXPOSED_API\":{        \"Methods\":[           {              \"name\":\"get_weather\",            \"description\":\"The method returns information about the weather in Athens, Greece\",            \"attributes\":[                 \"purpose\",               \"requester_id\"            ],            \"HTTP_REST_API\":{                 \"Method\":\"POST\",               \"Body\":{                    \"method_name\":\"get_weather\",                  \"attributes\":{                       \"$schema\":\"http://json-schema.org/draft-06/schema#\",                     \"title\":\"Attributes\",                     \"description\":\"The attributes of the exposed VDC method\",                     \"type\":\"object\",                     \"properties\":{                          \"purpose\":{                             \"type\":\"string\"                        },                        \"requester_id\":{                             \"type\":\"string\"                        }                     },                     \"additionalProperties\":false,                     \"required\":[                          \"purpose\",                        \"requester_id\"                     ]                  },                  \"example\":{                       \"method_name\":\"get_weather\",                     \"attributes\":{                          \"purpose\":\"research\",                        \"requester_id\":\"ntua\"                     }                  }               },               \"Headers\":[                    {                       \"Content-Type\":\"application/json\"                  }               ]            },            \"output_schema\":{                 \"$schema\":\"http://json-schema.org/draft-06/schema#\",               \"title\":\"weather in Athens, Greece\",               \"type\":\"object\",               \"properties\":{                    \"weather\":{                       \"type\":\"string\"                  },                  \"tempk\":{                       \"type\":\"number\",                     \"minimum\":0,                     \"description\":\"the temperature in Kelvin\"                  },                  \"tempc\":{                       \"type\":\"number\",                     \"minimum\":-273.15,                     \"description\":\"the temperature in Celsius\"                  },                  \"tempf\":{                       \"type\":\"number\",                     \"minimum\":-459.67,                     \"description\":\"the temperature in Fahrenheit\"                  },                  \"humidity\":{                       \"type\":\"string\",                     \"description\":\"this is a percentage\"                  },                  \"windspeed\":{                       \"type\":\"number\"                  },                  \"winddirection\":{                       \"type\":\"number\"                  },                  \"location\":{                       \"type\":\"string\"                  },                  \"epoch\":{                       \"type\":\"string\",                     \"description\":\"the observation time in epoch format\"                  },                  \"description\":{                       \"type\":\"string\"                  },                  \"forecast\":{                       \"type\":\"string\"                  }               },               \"additionalProperties\":false,               \"required\":[                    \"weather\",                  \"tempk\",                  \"tempc\",                  \"tempf\",                  \"humidity\",                  \"windspeed\",                  \"winddirection\",                  \"location\",                  \"epoch\",                  \"description\",                  \"forecast\"               ]            }         }      ],      \"URI\":{           \"format\":\"hostname:port/CAF\",         \"example\":\"localhost:1880/CAF\"      }   }    }"

  
});

//call the notify violations with the same ID of the VDC
before("/NotifyViolation > POST > 200", function (transaction) {
	  transaction.request.headers['Violations'] = "[ { \"vdcId\": \"1\", \"methodId\": \"getAllValuesForBloodTestComponent\", \"metrics\": [ { \"key\": \"availability\", \"value\": 90, \"datetime\": \"2018-08-07T10:50:23.674337517+02:00\"}]}]"
});

