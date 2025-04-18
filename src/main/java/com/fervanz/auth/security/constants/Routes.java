package com.fervanz.auth.security.constants;

import com.fervanz.auth.shared.constants.Api;

public abstract class Routes {
    public static String[] WHITE_LIST = {
            Api.V1_ROUTE + Api.HEALTH + "/check",
            Api.V1_ROUTE + Api.AUTH_ROUTE + "/sign-up",
            Api.V1_ROUTE + Api.AUTH_ROUTE + "/sign-in",
            Api.V1_ROUTE + Api.AUTH_ROUTE + "/request-password-reset",
            Api.V1_ROUTE + Api.AUTH_ROUTE + "/reset-password",
    };
}
