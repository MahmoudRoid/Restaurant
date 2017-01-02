package unicode.ir.restaurant.Classes;

public class Variables {

    public static final String Tag                  = "MYAPP";
    public static final String Domain               = "doctor.dotnetkaran.ir";

    public static final String TOKEN                = "Z4_ru@9j";

    public static final String catId                = "catId";
    public static final String Id                   = "Id";
    public static final String catIndex             = "catIndex";
    public static final String Index                = "Index";

    public static final String getNews              = "1";
    public static final String getDanestaniha       = "2";
    public static final String getServices          = "15"; //30
    public static final String getAboutUs           = "13"; // 4
    public static final String getImages            = "7";//11
    public static final String getVideos            = "8";//12
    public static final String getSliders           = "9";//28
    public static final String getVoices            = "9";
    public static final String getFiles             = "10";
    public static final String getMagazine          = "17";
    public static final String getInsurance         = "16"; //31
    public static final String getParts             = "14"; //29
    public static final String getCareBefore        = "19"; //24
    public static final String getCareAfter         = "20"; //20
    public static final String getDrugs             = "18"; //19
    public static final String getCommonQuestions   = "22"; //12


    // goods
    public static final String getFoods = "1";


    public static final String WebSite_Link         = "";
    public static final String Telegram_Channel_Id  = "";
    public static final String Instagram_Id         = "";
    public static final String Instagram_Id_From_App= "";
    public static final String Telegram_chat_Id     = "";
    public static final String Gmail_UniCode        = "unicode@gmail.com";
    public static final String GooglePlus_UniCode   = "https://plus.google.com/communities/107847486351510098159";

}// end class

/**
 * Both below two methods do the same thing :

 1 :
 News : (Objects)
 URL +/+ GetItem
 {catId:NEWS_ID,Token=TOKEN}

 News : (Folder)
 URL +/+ GetCategory
 {Id:NEWS_ID,Token=TOKEN}

 _______________________________

 2 :
 News : (Objects)
 URL +/+ GetItem
 {catIndex:NEWS_INDEX,Token=TOKEN}

 News : (Folder)
 URL +/+ GetCategory
 {Index:NEWS_INDEX,Token=TOKEN}


 ************************************
 * this method get all objects of folders and sub folder in the root folder with id or index mentioned

 News : (Objects and Folders)
 URL +/+ GetFullItems
 {Id:NEWS_ID / NEWS_INDEX,Token=TOKEN}


 ************************************
 * this method get all categories inside folder and sub folders of the root folder with id or index mentioned
 News : (Categories inside folder)
 URL +/+ GetFullCategories
 {Id:NEWS_ID / NEWS_INDEX,Token=TOKEN}


 **/
