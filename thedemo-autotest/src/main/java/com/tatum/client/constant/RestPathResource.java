package com.tatum.client.constant;

public class RestPathResource {

    final public static String BASE_PATH = "/webservice";

    final public static String PERSON_SERVICE = "/person";

    final public static String PERSON_WITHID_SERVICE = "/person/{" + RestParam.PERSON_ID +"}";

    final public static String PERSON_WITHBINARY_SERVICE = "/person/{" + RestParam.PERSON_ID + "}/binary";

}
