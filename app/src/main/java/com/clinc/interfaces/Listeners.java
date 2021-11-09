package com.clinc.interfaces;


public interface Listeners {




    interface BackListener
    {
        void back();
    }
    interface HomeListener {



        void previousOrder();

        void profile();


        void langChange();

    }
    interface ProfileActions
    {
        void onMyWallet();
        void onFavorite();
        void onAddress();
        void onChangeLanguage();
        void onTerms();
        void onContactUs();
        void onMenu();
        void onAddresses();
        void onFacebook();
        void onTwitter();
        void onInstagram();
        void onLogout();

    }


}
