/**
 * HttpEntityEnclosingDeleteRequest.java
 * cn.videoworks.edge.util
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年7月21日 		meishen
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package cn.videoworks.cms.util;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * ClassName:HttpEntityEnclosingDeleteRequest
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.0.0
 * @Date	 2017年7月21日		上午11:29:39
 *
 */
public class HttpEntityEnclosingDeleteRequest extends
		HttpEntityEnclosingRequestBase {

	public HttpEntityEnclosingDeleteRequest(final URI uri) {
        super();
        setURI(uri);
    }
 
    @Override
    public String getMethod() {
        return "DELETE";
    }

}

