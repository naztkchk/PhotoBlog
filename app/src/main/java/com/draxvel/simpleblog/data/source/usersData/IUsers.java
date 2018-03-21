package com.draxvel.simpleblog.data.source.usersData;

public interface IUsers {

    interface CurrentUserExistsCallback
    {
        void onNotExists();
    }

    void isCurrentUserExists(CurrentUserExistsCallback currentUserExistsCallback);



}
