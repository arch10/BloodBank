package com.gigaworks.bloodbankbeta;

import java.util.ArrayList;

/**
 * Created by Arch on 13-04-2017.
 */

public interface MapCallback {
    void onSuccess(ArrayList<String> info);

    void onFail(String message);
}
