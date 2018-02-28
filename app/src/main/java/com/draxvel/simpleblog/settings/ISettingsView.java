package com.draxvel.simpleblog.settings;

import com.draxvel.simpleblog.IView;

public interface ISettingsView extends IView{

    void setName(final String name);
    void setImage(final String image);

    void setEnabledSaveBtn(final boolean b);

    void showMainActivity();
}
