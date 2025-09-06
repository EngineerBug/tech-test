package org.hmxlabs.techtest;

public class Constant {

    public static final String DUMMY_DATA = "AKCp5fU4WNWKBVvhXsbNhqk33tawri9iJUkA5o4A6YqpwvAoYjajVw8xdEw6r9796h1wEp29D";

    private static final String ADDRESS = "http://localhost:8090/";

	public static final String URI_ISOK = ADDRESS + "dataserver/isok";
    public static final String URI_PUSHDATA = ADDRESS + "dataserver/pushdata";
    public static final String URI_GETDATA = ADDRESS + "dataserver/data/{blockType}";
    public static final String URI_PATCHDATA = ADDRESS + "dataserver/update/{name}/{newBlockType}";
}
