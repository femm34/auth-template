package com.fervanz.auth.security.constants;

import com.fervanz.auth.shared.constants.Api;

public abstract class Routes {
    public static String[] WHITE_LIST = {
            Api.V1_ROUTE + Api.HEALTH + "/check",
    };
}
