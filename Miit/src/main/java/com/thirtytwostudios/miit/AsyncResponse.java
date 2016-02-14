package com.thirtytwostudios.miit;

import org.json.JSONObject;

/**
 * Created by timgrohmann on 13.02.16.
 */
public interface AsyncResponse {
    void processFinish(JSONObject output);
}

