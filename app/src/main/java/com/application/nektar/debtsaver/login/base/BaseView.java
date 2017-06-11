package com.application.nektar.debtsaver.login.base;

/**
 * Created by Aleksander on 11.06.2017.
 */

import android.support.annotation.NonNull;

public interface BaseView<T> {

    void setPresenter(@NonNull T presenter);
}
