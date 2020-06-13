package nithra.jobs.career.placement.utills;

/**
 * @author மயூரேசன்
 * @குறிப்பு யுனிக்கோட் முறையில் இருக்கும் சொற்கள் வசனங்களை பாமினி, திஸ்கி, டாப், டாம் போன்ற
 * குறிமுறைகளுக்கு மாற்றியமைக்க இந்த செயலி உங்களுக்கு உதவும்
 */
public class CodetoTamilUtil {
    /**
     * பாமினி முறையில் அமைந்த குறியேற்றம்
     */
    public final static int BAMINI = 0;
    /**
     * திஸ்கி முறையில் அமைந்த குறியேற்றம்
     */
    public final static int TSCII = 1;
    /**
     * அஞ்சல் முறையில் அமைந்த குறியேற்றம்
     */
    public final static int ANJAL = 2;
    /**
     * டாப் முறையில் அமைந்த குறியேற்றம்
     */
    public final static int TAB = 3;
    /**
     * டாம் முறையில் அமைந்த குறியேற்றம்
     */
    public final static int TAM = 4;

    /**
     * interchange words BAMINI
     */

    /*
     * இந்த method இன் மூலம் நீங்கள் யுனிக்கோட் குறிமுறையில் இருந்து உங்களுக்குத்
     * தேவையான குறிமுறைக்கு சொற்கள், வசனங்களை மாற்றிக் கொள்ளலாம்.
     * @param encodCode - எந்தக் குறிமுறைக்கு மாற்ற விரும்புகின்றீர்கள் என்பதை இங்கே குறிப்பிட வேண்டும்.
     * உதாரணமாக பாமினி முறை என்றால் TamilUtil.BAMINI என்றோ அல்லது திஸ்கி முறைமை என்றால் TamilUtil.TSCII* என்றோ குறிப்பிடலாம்.
     *
     * @param unicodeStr யுனிக்கோட் சொற்கள் வசனங்கள்
     * @return யுனிக்கோடில் இருந்து நீங்கள் குறிப்பிட்ட குறிமுறைக்கு மாற்றப்பட்ட சொற்கள், வசனங்கள் உங்களுக்கு வந்து கிடைக்கும்.
     */
    public static String convertToTamil(int encodCode, String unicodeStr) {
        String convertedStr = "";
        CodetoTamilUtil tu = new CodetoTamilUtil();
        if (encodCode == CodetoTamilUtil.BAMINI) {
            convertedStr = tu.convertToBamini(unicodeStr);
        } else if (encodCode == CodetoTamilUtil.TSCII) {
            convertedStr = tu.convertToTSCII(unicodeStr);
        } else if (encodCode == CodetoTamilUtil.ANJAL) {
            convertedStr = tu.convertToAnjal(unicodeStr);
        } else if (encodCode == CodetoTamilUtil.TAB) {
            convertedStr = tu.convertToTab(unicodeStr);
        } else if (encodCode == CodetoTamilUtil.TAM) {
            convertedStr = tu.convertToTam(unicodeStr);
        }
        return convertedStr;
    }

    private String convertToBamini(String unicodeStr) {
        unicodeStr = unicodeStr.replace("sp", "ளி");
        unicodeStr = unicodeStr.replace("hp", "ரி");
        unicodeStr = unicodeStr.replace("hP", "ரீ");
        unicodeStr = unicodeStr.replace("uP", "ரீ");
        unicodeStr = unicodeStr.replace("u;", "ர்");
        unicodeStr = unicodeStr.replace("h;", "ர்");
        unicodeStr = unicodeStr.replace("H", "ர்");

        unicodeStr = unicodeStr.replace("nfs", "கௌ");
        unicodeStr = unicodeStr.replace("Nfh", "கோ");
        unicodeStr = unicodeStr.replace("nfh", "கொ");
        unicodeStr = unicodeStr.replace("fh", "கா");
        unicodeStr = unicodeStr.replace("fp", "கி");
        unicodeStr = unicodeStr.replace("fP", "கீ");
        unicodeStr = unicodeStr.replace("F", "கு");
        unicodeStr = unicodeStr.replace("$", "கூ");
        unicodeStr = unicodeStr.replace("nf", "கெ");
        unicodeStr = unicodeStr.replace("Nf", "கே");
        unicodeStr = unicodeStr.replace("if", "கை");
        unicodeStr = unicodeStr.replace("f;", "க்");
        unicodeStr = unicodeStr.replace("f", "க");

        unicodeStr = unicodeStr.replace("nqs", "ஙௌ");
        unicodeStr = unicodeStr.replace("Nqh", "ஙோ");
        unicodeStr = unicodeStr.replace("nqh", "ஙொ");
        unicodeStr = unicodeStr.replace("qh", "ஙா");
        unicodeStr = unicodeStr.replace("qp", "ஙி");
        unicodeStr = unicodeStr.replace("qP", "ஙீ");
        unicodeStr = unicodeStr.replace("nq", "ஙெ");
        unicodeStr = unicodeStr.replace("Nq", "ஙே");
        unicodeStr = unicodeStr.replace("iq", "ஙை");
        unicodeStr = unicodeStr.replace("q;", "ங்");
        unicodeStr = unicodeStr.replace("q", "ங");///

        unicodeStr = unicodeStr.replace("nrs", "சௌ");
        unicodeStr = unicodeStr.replace("Nrh", "சோ");
        unicodeStr = unicodeStr.replace("nrh", "சொ");
        unicodeStr = unicodeStr.replace("rh", "சா");
        unicodeStr = unicodeStr.replace("rp", "சி");
        unicodeStr = unicodeStr.replace("rP", "சீ");
        unicodeStr = unicodeStr.replace("R", "சு");
        unicodeStr = unicodeStr.replace("#", "சூ");
        unicodeStr = unicodeStr.replace("nr", "செ");
        unicodeStr = unicodeStr.replace("Nr", "சே");
        unicodeStr = unicodeStr.replace("ir", "சை");
        unicodeStr = unicodeStr.replace("r;", "ச்");
        unicodeStr = unicodeStr.replace("r", "ச");//


        unicodeStr = unicodeStr.replace("n[s", "ஜௌ");
        unicodeStr = unicodeStr.replace("N[h", "ஜோ");
        unicodeStr = unicodeStr.replace("n[h", "ஜொ");
        unicodeStr = unicodeStr.replace("[h", "ஜா");
        unicodeStr = unicodeStr.replace("[p", "ஜி");
        unicodeStr = unicodeStr.replace("[P", "ஜீ");
        unicodeStr = unicodeStr.replace("[{", "ஜு");
        unicodeStr = unicodeStr.replace("[_", "ஜூ");//

        unicodeStr = unicodeStr.replace("n[", "ஜெ");
        unicodeStr = unicodeStr.replace("N[", "ஜே");
        unicodeStr = unicodeStr.replace("i[", "ஜை");
        unicodeStr = unicodeStr.replace("[;", "ஜ்");
        unicodeStr = unicodeStr.replace("[", "ஜ");//

        unicodeStr = unicodeStr.replace("ஜ{", "ஜு");
        unicodeStr = unicodeStr.replace("ஜ_", "ஜூ");


        unicodeStr = unicodeStr.replace("nQs", "ஞௌ");
        unicodeStr = unicodeStr.replace("NQh", "ஞோ");
        unicodeStr = unicodeStr.replace("nQh", "ஞொ");
        unicodeStr = unicodeStr.replace("Qh", "ஞா");
        unicodeStr = unicodeStr.replace("Qp", "ஞி");
        unicodeStr = unicodeStr.replace("QP", "ஞீ");
        unicodeStr = unicodeStr.replace("nQ", "ஞெ");
        unicodeStr = unicodeStr.replace("NQ", "ஞே");
        unicodeStr = unicodeStr.replace("iQ", "ஞை");
        unicodeStr = unicodeStr.replace("Q;", "ஞ்");
        unicodeStr = unicodeStr.replace("Q", "ஞ");///

        unicodeStr = unicodeStr.replace("nls", "டௌ");
        unicodeStr = unicodeStr.replace("Nlh", "டோ");
        unicodeStr = unicodeStr.replace("nlh", "டொ");
        unicodeStr = unicodeStr.replace("lp", "டி");
        unicodeStr = unicodeStr.replace("lP", "டீ");
        unicodeStr = unicodeStr.replace("lh", "டா");
        unicodeStr = unicodeStr.replace("b", "டி");
        unicodeStr = unicodeStr.replace("B", "டீ");
        unicodeStr = unicodeStr.replace("L", "டு");
        unicodeStr = unicodeStr.replace("^", "டூ");
        unicodeStr = unicodeStr.replace("nl", "டெ");
        unicodeStr = unicodeStr.replace("Nl", "டே");
        unicodeStr = unicodeStr.replace("il", "டை");
        unicodeStr = unicodeStr.replace("l;", "ட்");
        unicodeStr = unicodeStr.replace("l", "ட");////

        unicodeStr = unicodeStr.replace("nzs", "ணௌ");
        unicodeStr = unicodeStr.replace("Nzh", "ணோ");
        unicodeStr = unicodeStr.replace("nzh", "ணொ");
        unicodeStr = unicodeStr.replace("zh", "ணா");
        unicodeStr = unicodeStr.replace("zp", "ணி");
        unicodeStr = unicodeStr.replace("zP", "ணீ");


        unicodeStr = unicodeStr.replace("Zh", "ணூ");
        unicodeStr = unicodeStr.replace("Z}", "ணூ");

        unicodeStr = unicodeStr.replace("nz", "ணெ");
        unicodeStr = unicodeStr.replace("Nz", "ணே");
        unicodeStr = unicodeStr.replace("iz", "ணை");


        unicodeStr = unicodeStr.replace("z;", "ண்");
        unicodeStr = unicodeStr.replace("Z", "ணு");
        unicodeStr = unicodeStr.replace("z", "ண");////


        unicodeStr = unicodeStr.replace("njs", "தௌ");
        unicodeStr = unicodeStr.replace("Njh", "தோ");
        unicodeStr = unicodeStr.replace("njh", "தொ");
        unicodeStr = unicodeStr.replace("jh", "தா");
        unicodeStr = unicodeStr.replace("jp", "தி");
        unicodeStr = unicodeStr.replace("jP", "தீ");
        unicodeStr = unicodeStr.replace("Jh", "தூ");
        unicodeStr = unicodeStr.replace("Jh", "தூ");
        unicodeStr = unicodeStr.replace("J}", "தூ");
        unicodeStr = unicodeStr.replace("J", "து");
        unicodeStr = unicodeStr.replace("nj", "தெ");
        unicodeStr = unicodeStr.replace("Nj", "தே");
        unicodeStr = unicodeStr.replace("ij", "தை");
        unicodeStr = unicodeStr.replace("j;", "த்");
        unicodeStr = unicodeStr.replace("j", "த");//

        unicodeStr = unicodeStr.replace("nes", "நௌ");
        unicodeStr = unicodeStr.replace("Neh", "நோ");
        unicodeStr = unicodeStr.replace("neh", "நொ");
        unicodeStr = unicodeStr.replace("eh", "நா");
        unicodeStr = unicodeStr.replace("ep", "நி");
        unicodeStr = unicodeStr.replace("eP", "நீ");
        unicodeStr = unicodeStr.replace("E}", "நூ");
        unicodeStr = unicodeStr.replace("Eh", "நூ");
        unicodeStr = unicodeStr.replace("E", "நு");
        unicodeStr = unicodeStr.replace("ne", "நெ");
        unicodeStr = unicodeStr.replace("Ne", "நே");
        unicodeStr = unicodeStr.replace("ie", "நை");
        unicodeStr = unicodeStr.replace("e;", "ந்");
        unicodeStr = unicodeStr.replace("e", "ந");//


        unicodeStr = unicodeStr.replace("nds", "னௌ");
        unicodeStr = unicodeStr.replace("Ndh", "னோ");
        unicodeStr = unicodeStr.replace("ndh", "னொ");
        unicodeStr = unicodeStr.replace("dh", "னா");
        unicodeStr = unicodeStr.replace("dp", "னி");
        unicodeStr = unicodeStr.replace("dP", "னீ");
        unicodeStr = unicodeStr.replace("D}", "னூ");

        unicodeStr = unicodeStr.replace("Dh", "னூ");
        unicodeStr = unicodeStr.replace("D", "னு");
        unicodeStr = unicodeStr.replace("nd", "னெ");
        unicodeStr = unicodeStr.replace("Nd", "னே");
        unicodeStr = unicodeStr.replace("id", "னை");
        unicodeStr = unicodeStr.replace("d;", "ன்");
        unicodeStr = unicodeStr.replace("d", "ன");//

        unicodeStr = unicodeStr.replace("ngs", "பௌ");
        unicodeStr = unicodeStr.replace("Ngh", "போ");
        unicodeStr = unicodeStr.replace("ngh", "பொ");
        unicodeStr = unicodeStr.replace("gh", "பா");
        unicodeStr = unicodeStr.replace("gp", "பி");
        unicodeStr = unicodeStr.replace("gP", "பீ");


        unicodeStr = unicodeStr.replace("G", "பு");
        unicodeStr = unicodeStr.replace("ng", "பெ");
        unicodeStr = unicodeStr.replace("Ng", "பே");
        unicodeStr = unicodeStr.replace("ig", "பை");
        unicodeStr = unicodeStr.replace("g;", "ப்");
        unicodeStr = unicodeStr.replace("g", "ப");//

        unicodeStr = unicodeStr.replace("nks", "மௌ");
        unicodeStr = unicodeStr.replace("Nkh", "மோ");
        unicodeStr = unicodeStr.replace("nkh", "மொ");
        unicodeStr = unicodeStr.replace("kh", "மா");
        unicodeStr = unicodeStr.replace("kp", "மி");
        unicodeStr = unicodeStr.replace("kP", "மீ");
        unicodeStr = unicodeStr.replace("K", "மு");
        unicodeStr = unicodeStr.replace("%", "மூ");
        unicodeStr = unicodeStr.replace("nk", "மெ");
        unicodeStr = unicodeStr.replace("Nk", "மே");
        unicodeStr = unicodeStr.replace("ik", "மை");
        unicodeStr = unicodeStr.replace("k;", "ம்");
        unicodeStr = unicodeStr.replace("k", "ம");//


        unicodeStr = unicodeStr.replace("nas", "யௌ");
        unicodeStr = unicodeStr.replace("Nah", "யோ");
        unicodeStr = unicodeStr.replace("nah", "யொ");
        unicodeStr = unicodeStr.replace("ah", "யா");
        unicodeStr = unicodeStr.replace("ap", "யி");
        unicodeStr = unicodeStr.replace("aP", "யீ");
        unicodeStr = unicodeStr.replace("A", "யு");
        unicodeStr = unicodeStr.replace("A+", "யூ");
        unicodeStr = unicodeStr.replace("na", "யெ");
        unicodeStr = unicodeStr.replace("Na", "யே");
        unicodeStr = unicodeStr.replace("ia", "யை");
        unicodeStr = unicodeStr.replace("a;", "ய்");
        unicodeStr = unicodeStr.replace("a", "ய");//

        unicodeStr = unicodeStr.replace("nus", "ரௌ");
        unicodeStr = unicodeStr.replace("Nuh", "ரோ");
        unicodeStr = unicodeStr.replace("nuh", "ரொ");
        unicodeStr = unicodeStr.replace("uh", "ரா");
        unicodeStr = unicodeStr.replace("up", "ரி");


        unicodeStr = unicodeStr.replace("U", "ரு");
        unicodeStr = unicodeStr.replace("&", "ரூ");
        unicodeStr = unicodeStr.replace("nu", "ரெ");
        unicodeStr = unicodeStr.replace("Nu", "ரே");
        unicodeStr = unicodeStr.replace("iu", "ரை");

        unicodeStr = unicodeStr.replace("u", "ர");//

        unicodeStr = unicodeStr.replace("nys", "லௌ");
        unicodeStr = unicodeStr.replace("Nyh", "லோ");
        unicodeStr = unicodeStr.replace("nyh", "லொ");
        unicodeStr = unicodeStr.replace("yh", "லா");
        unicodeStr = unicodeStr.replace("yp", "லி");
        unicodeStr = unicodeStr.replace("yP", "லீ");
        unicodeStr = unicodeStr.replace("Yh", "லூ");
        unicodeStr = unicodeStr.replace("Y}", "லூ");
        unicodeStr = unicodeStr.replace("Y", "லு");
        unicodeStr = unicodeStr.replace("ny", "லெ");
        unicodeStr = unicodeStr.replace("Ny", "லே");
        unicodeStr = unicodeStr.replace("iy", "லை");
        unicodeStr = unicodeStr.replace("y;", "ல்");
        unicodeStr = unicodeStr.replace("y", "ல");//

        unicodeStr = unicodeStr.replace("nss", "ளௌ");
        unicodeStr = unicodeStr.replace("Nsh", "ளோ");
        unicodeStr = unicodeStr.replace("nsh", "ளொ");
        unicodeStr = unicodeStr.replace("sh", "ளா");

        unicodeStr = unicodeStr.replace("sP", "ளீ");
        unicodeStr = unicodeStr.replace("Sh", "ளூ");
        unicodeStr = unicodeStr.replace("S", "ளு");
        unicodeStr = unicodeStr.replace("ns", "ளெ");
        unicodeStr = unicodeStr.replace("Ns", "ளே");
        unicodeStr = unicodeStr.replace("is", "ளை");
        unicodeStr = unicodeStr.replace("s;", "ள்");
        unicodeStr = unicodeStr.replace("s", "ள");//


        unicodeStr = unicodeStr.replace("ntt", "வௌ");
        unicodeStr = unicodeStr.replace("Nth", "வோ");
        unicodeStr = unicodeStr.replace("nth", "வொ");
        unicodeStr = unicodeStr.replace("th", "வா");
        unicodeStr = unicodeStr.replace("tp", "வி");
        unicodeStr = unicodeStr.replace("tP", "வீ");


        unicodeStr = unicodeStr.replace("nt", "வெ");
        unicodeStr = unicodeStr.replace("Nt", "வே");
        unicodeStr = unicodeStr.replace("it", "வை");

        unicodeStr = unicodeStr.replace("t;", "வ்");
        unicodeStr = unicodeStr.replace("t", "வ");//


        unicodeStr = unicodeStr.replace("noo", "ழௌ");
        unicodeStr = unicodeStr.replace("Noh", "ழோ");
        unicodeStr = unicodeStr.replace("noh", "ழொ");
        unicodeStr = unicodeStr.replace("oh", "ழா");
        unicodeStr = unicodeStr.replace("op", "ழி");
        unicodeStr = unicodeStr.replace("oP", "ழீ");
        unicodeStr = unicodeStr.replace("*", "ழூ");
        unicodeStr = unicodeStr.replace("O", "ழு");
        unicodeStr = unicodeStr.replace("no", "ழெ");
        unicodeStr = unicodeStr.replace("No", "ழே");
        unicodeStr = unicodeStr.replace("io", "ழை");
        unicodeStr = unicodeStr.replace("o;", "ழ்");
        unicodeStr = unicodeStr.replace("o", "ழ");//

        unicodeStr = unicodeStr.replace("nws", "றௌ");
        unicodeStr = unicodeStr.replace("Nwh", "றோ");
        unicodeStr = unicodeStr.replace("nwh", "றொ");
        unicodeStr = unicodeStr.replace("wh", "றா");
        unicodeStr = unicodeStr.replace("wp", "றி");
        unicodeStr = unicodeStr.replace("wP", "றீ");
        unicodeStr = unicodeStr.replace("Wh", "றூ");
        unicodeStr = unicodeStr.replace("W}", "றூ");

        unicodeStr = unicodeStr.replace("W", "று");
        unicodeStr = unicodeStr.replace("nw", "றெ");
        unicodeStr = unicodeStr.replace("Nw", "றே");
        unicodeStr = unicodeStr.replace("iw", "றை");
        unicodeStr = unicodeStr.replace("w;", "ற்");
        unicodeStr = unicodeStr.replace("w", "ற");//

        unicodeStr = unicodeStr.replace("n``", "ஹௌ");
        unicodeStr = unicodeStr.replace("N`h", "ஹோ");
        unicodeStr = unicodeStr.replace("n`h", "ஹொ");
        unicodeStr = unicodeStr.replace("`h", "ஹா");
        unicodeStr = unicodeStr.replace("`p", "ஹி");
        unicodeStr = unicodeStr.replace("`P", "ஹீ");

        unicodeStr = unicodeStr.replace("n`", "ஹெ");
        unicodeStr = unicodeStr.replace("N`", "ஹே");
        unicodeStr = unicodeStr.replace("i`", "ஹை");
        unicodeStr = unicodeStr.replace("`;", "ஹ்");
        unicodeStr = unicodeStr.replace("`", "ஹ");//

        unicodeStr = unicodeStr.replace("ஹ{", "ஹு");
        unicodeStr = unicodeStr.replace("ஹ_", "ஹூ");

        unicodeStr = unicodeStr.replace("n~s", "ஷௌ");
        unicodeStr = unicodeStr.replace("N~h", "ஷோ");
        unicodeStr = unicodeStr.replace("n~h", "ஷொ");
        unicodeStr = unicodeStr.replace("~h", "ஷா");
        unicodeStr = unicodeStr.replace("~p", "ஷி");
        unicodeStr = unicodeStr.replace("~P", "ஷீ");

        unicodeStr = unicodeStr.replace("n~", "ஷெ");
        unicodeStr = unicodeStr.replace("N~", "ஷே");
        unicodeStr = unicodeStr.replace("i~", "ஷை");
        unicodeStr = unicodeStr.replace("\\;", "ஷ்");
        unicodeStr = unicodeStr.replace("\\", "ஷ");//
        unicodeStr = unicodeStr.replace("~;", "ஷ்");
        unicodeStr = unicodeStr.replace("~", "ஷ");//

        unicodeStr = unicodeStr.replace("{", "ஷு");
        unicodeStr = unicodeStr.replace("ஷ_", "ஷூ");
        unicodeStr = unicodeStr.replace("~", "ஷ");//


        unicodeStr = unicodeStr.replace("n]s", "ஸௌ");
        unicodeStr = unicodeStr.replace("N]h", "ஸோ");
        unicodeStr = unicodeStr.replace("n]h", "ஸொ");
        unicodeStr = unicodeStr.replace("]h", "ஸா");
        unicodeStr = unicodeStr.replace("]p", "ஸி");
        unicodeStr = unicodeStr.replace("]P", "ஸீ");

        unicodeStr = unicodeStr.replace("n]", "ஸெ");
        unicodeStr = unicodeStr.replace("N]", "ஸே");
        unicodeStr = unicodeStr.replace("i]", "ஸை");
        unicodeStr = unicodeStr.replace("];", "ஸ்");
        unicodeStr = unicodeStr.replace("]", "ஸ");//

        unicodeStr = unicodeStr.replace("ஸ{", "ஸு");
        unicodeStr = unicodeStr.replace("ஸ_", "ஸூ");


        unicodeStr = unicodeStr.replace("m", "அ");
        unicodeStr = unicodeStr.replace("M", "ஆ");
        unicodeStr = unicodeStr.replace("&lt;", "ஈ");
        unicodeStr = unicodeStr.replace("<", "ஈ");
        unicodeStr = unicodeStr.replace("c", "உ");
        unicodeStr = unicodeStr.replace("C", "ஊ");
        unicodeStr = unicodeStr.replace("v", "எ");
        unicodeStr = unicodeStr.replace("V", "ஏ");
        unicodeStr = unicodeStr.replace("I", "ஐ");
        unicodeStr = unicodeStr.replace("x", "ஒ");
        unicodeStr = unicodeStr.replace("X", "ஓ");
        unicodeStr = unicodeStr.replace("xs", "ஔ");


        unicodeStr = unicodeStr.replace("/", "ஃ");

        unicodeStr = unicodeStr.replace("@", ";");

        unicodeStr = unicodeStr.replace(",", "இ");

        unicodeStr = unicodeStr.replace("=", "ஸ்ரீ");

        unicodeStr = unicodeStr.replace(">", ",");

        unicodeStr = unicodeStr.replace("T", "வு");

        unicodeStr = unicodeStr.replace("சு+", "சூ");
        unicodeStr = unicodeStr.replace("வு+", "வூ");
        unicodeStr = unicodeStr.replace("வ+", "வூ");
        unicodeStr = unicodeStr.replace("G+", "பூ");
        unicodeStr = unicodeStr.replace("ப+", "பூ");
        unicodeStr = unicodeStr.replace("பு+", "பூ");
        unicodeStr = unicodeStr.replace("A+", "யூ");
        unicodeStr = unicodeStr.replace("யு+", "யூ");
        unicodeStr = unicodeStr.replace("ய+", "யூ");
        return unicodeStr;
    }

    private String convertToTSCII(String unicodeStr) {
        String[] TamilText = {
                "·", "«", "¬", "þ", "®", "¯", "°", "±", "²", "³", "´", "µ", "¶",

                "ì", "¸", "¸¡", "¸¢", "¸£", "Ì", "Ü", "¦¸", "§¸", "¨¸", "¦¸¡", "§¸¡", "¦¸ª",
                "í", "¹", "¹¡", "¹¢", "¹£", "™", "›", "¦¹", "§¹", "¨¹", "¦¹¡", "§¹¡", "¦¹ª",
                "î", "º", "º¡", "º¢", "º£", "Í", "Ý", "¦º", "§º", "¨º", "¦º¡", "§º¡", "¦ºª",
                "ï", "»", "»¡", "»¢", "»£", "š", "œ", "¦»", "§»", "¨»", "¦»¡", "§»¡", "¦»ª",
                "ð", "¼", "¼¡", " Ê", "Ë", "Î", "Þ", "¦¼", "§¼", "¨¼", "¦¼¡", "§¼¡", "¦¼ª",
                "ñ", "½", "½¡", "½¢", "	½£", "Ï", "ß", "¦½", "§½", "¨½", "¦½¡", "§½¡", "¦½ª",
                "ò", "¾", "¾¡", "¾¢", "¾£", "Ð", "à", "¦¾", "§¾", "¨¾", "¦¾¡", "§¾¡", "¦¾ª",
                "ó", "¿", "¿¡", "¿¢", "¿£", "Ñ", "á", "¦¿", "§¿", "¨¿", "¦¿¡", "§¿¡", "¦¿ª",
                "ô", "À", "À¡", "À¢", "À£", "Ò", "â", "¦À", "§À", "¨À", "¦À¡", "§À¡", "¦Àª",
                "õ", "Á", "Á¡", "Á¢", "Á£", "Ó", "ã", "¦Á", "§Á", "¨Á", "¦Á¡", "§Á¡", "¦Áª",
                "ö", "Â", "Â¡", "Â¢", "Â£", "Ô", "ä", "¦Â", "§Â", "¨Â", "¦Â¡", "§Â¡", "¦Âª",
                "÷", "Ã", "Ã¡", "Ã¢", "Ã£", "Õ", "å", "¦Ã", "§Ã", "¨Ã", "¦Ã¡", "§Ã¡", "¦Ãª",
                "ø", "Ä", "Ä¡", "Ä¢", "Ä£", "Ö", "æ", "¦Ä", "§Ä", "¨Ä", "¦Ä¡", "§Ä¡", "¦Äª",
                "ù", "Å", "Å¡", "Å¢", "Å£", "×", "ç", "¦Å", "§Å", "¨Å", "¦Å¡", "§Å¡", "¦Åª",
                "ú", "Æ", "Æ¡", "Æ¢", "Æ£", "Ø", "è", "¦Æ", "§Æ", "¨Æ", "¦Æ¡", "§Æ¡", "¦Æª",
                "û", "Ç", "Ç¡", "Ç¢", "Ç£", "Ù", "é", "¦Ç", "§Ç", "¨Ç", "¦Ç¡", "§Ç¡", "¦Çª",
                "ü", "È", "È¡", "È¢", "È£", "Ú", "ê", "¦È", "§È", "¨È", "¦È¡", "§È¡", "¦Èª",
                "ý", "É", "É¡", "É¢", "É£", "Û", "ë", "¦É", "§É", "¨É", "¦É¡", "§É¡", "¦Éª",

                "ˆ", "ƒ", "ƒ¡", "ƒ¢", "ƒ£", "ƒ¤", "ƒ¥", "¦ƒ", "§ƒ", "¨ƒ", "¦ƒ¡", "§ƒ¡", "¦ƒª",
                "‰", "„", "„¡", "„¢", "„£", "„¤", "„¥", "¦„", "§„", "¨„", "¦„¡", "§„¡", "¦„ª",
                "Š", "…", "…¡", "…¢", "…£", "…¤", "…¥", "¦…", "§…", "¨…", "¦…¡", "§…¡", "¦…ª",
                "‹", "†", "†¡", "†¢", "†£", "†¤", "†¥", "¦†", "§†", "¨†", "¦†¡", "§†¡", "¦†ª",
                "Œ", "‡", "‡¡", "‡¢", "‡£", "‡¤", "‡¥", "¦‡", "§‡", "¨‡", "¦‡¡", "§‡¡", "¦‡ª"

        };

        String[] TamilReplace = {
                "ஃ", "அ", "ஆ", "இ", "ஈ", "உ", "ஊ", "	எ", "ஏ", "ஐ", "ஒ", "ஓ", "ஔ",

                "க்", "க", "கா", "கி", "கீ", "கு", "கூ", "கெ", "கே", "கை", "கொ", "கோ", "கௌ",
                "ங்", "ங", "ஙா", "ஙி", "ஙீ", "ஙு", "ஙூ", "ஙெ", "ஙே", "ஙை", "ஙொ", "ஙோ", "ஙௌ",
                "ச்", "ச", "சா", "சி", "சீ", "சு", "சூ", "செ", "சே", "சை", "சொ", "	சோ", "சௌ",
                "ஞ்", "ஞ", "ஞா", "	ஞி", "ஞீ", "ஞு", "ஞூ", "ஞெ", "ஞே", "ஞை", "ஞொ", "ஞோ", "ஞௌ",
                "ட்", "ட", "டா", "டி", "டீ", "டு", "	டூ", "டெ", "டே", "டை", "டொ", "டோ", "டௌ	",
                "ண்", "ண", "ணா", "ணி", "ணீ", "ணு", "ணூ", "ணெ", "ணே", "ணை", "ணொ", "ணோ", "ணௌ",
                "த்", "த", "தா", "தி", "தீ", "து", "தூ", "தெ", "தே", "தை", "தொ", "தோ", "தௌ",
                "ந்", "ந", "நா", "நி", "நீ", "நு", "நூ", "நெ", "நே", "நை", "நொ", "நோ", "நௌ",
                "ப்", "ப", "பா", "பி", "பீ", "பு", "பூ", "பெ", "பே", "பை", "பொ", "	போ", "பௌ",
                "ம்", "ம", "மா", "மி", "	மீ", "மு", "மூ", "மெ", "மே", "மை", "மொ", "மோ", "மௌ",
                "ய்", "ய", "யா", "யி", "	யீ", "யு", "யூ", "யெ", "யே", "	யை", "யொ", "யோ", "யௌ",
                "ர்", "ர", "ரா", "ரி", "ரீ", "ரு", "ரூ", "ரெ", "	ரே", "ரை", "ரொ", "ரோ", "ரௌ",
                "ல்", "ல", "லா", "லி", "	லீ", "லு", "லூ", "லெ", "	லே", "லை", "லொ", "லோ", "லௌ",
                "வ்", "வ", "வா", "வி", "	வீ", "வு", "வூ", "வெ", "வே", "வை", "வொ", "வோ", "வௌ",
                "ழ்", "ழ", "ழா", "ழி", "	ழீ", "ழு", "ழூ", "ழெ", "ழே", "ழை", "ழொ", "ழோ", "ழௌ",
                "ள்", "ள", "ளா", "ளி", "ளீ", "ளு", "ளூ", "ளெ", "ளே", "ளை", "ளொ", "ளோ", "ளௌ",
                "ற்", "ற", "றா", "றி", "றீ", "று", "றூ", "றெ", "றே", "றை", "றொ", "றோ", "றௌ",
                "ன்", "ன", "னா", "னி", "னீ", "னு", "னூ", "னெ", "னே", "னை", "னொ	", "னோ", "னௌ",

                "ஜ்", "ஜ", "ஜா", "ஜி", "ஜீ", "ஜு", "ஜூ", "ஜெ", "ஜே", "ஜை", "ஜொ", "ஜோ", "ஜௌ",
                "ஷ்", "ஷ", "ஷா", "ஷி", "ஷீ", "ஷு", "	ஷூ", "ஷெ", "	ஷே", "ஷை", "ஷொ", "ஷோ", "ஷௌ",
                "ஸ்", "ஸ", "ஸா", "ஸி", "ஸீ", "ஸு", "ஸூ", "ஸெ", "ஸே", "ஸை", "ஸொ", "ஸோ", "ஸௌ",
                "ஹ்", "ஹ", "ஹா", "ஹி", "ஹீ", "ஹு", "ஹூ", "ஹெ", "ஹே", "ஹை", "ஹொ", "ஹோ", "ஹௌ",
                "க்ஷ்", "க்ஷ", "	க்ஷா", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷை", "க்ஷொ", "க்ஷோ", "க்ஷௌ"

        };
        int count = 0;
        while (count < TamilText.length) {
            unicodeStr = unicodeStr.replaceAll(TamilText[count], TamilReplace[count]);
            count++;
        }
        return unicodeStr;
    }

    private String convertToAnjal(String unicodeStr) {
        String[] TamilText = {"û", "û‘", "û’", "û“", "û”", "û•", "—û", "þû", "—û‘", "÷ñõ",
                "—ûã", "û", "ü", "—óó", "þó‘", "—ó‘", "ó‘", "ó", "ó’", "ó",
                "ó”", "ó•", "—ó", "þó", "€ó", "ô", "ó", "—ã", "þ‘", "—‘",
                "‘", "Ž", "", "", "˜", "—", "þ", "€", "™",
                "", "—šã", "þš‘", "—š‘", "š‘", "š", "š", "—š", "þš", "€š",
                "›", "š", "—œã", "þœ‘", "—œ‘", "œ‘", "", "", "Ÿ", "¡",
                "—œ", "þœ", "€œ", "¢", "œ", "œ", "—£ã", "þ£‘", "—£‘", "£‘",
                "£", "£", "—£", "þ£", "€£", "¤", "£", "—¥ã", "þ¥‘", "—¥‘",
                "¥‘", "¦", "§", "¨", "©", "—¥", "þ¥", "€¥", "ª", "¥",
                "—«ã", "þ«‘", "—«‘", "«‘", "¬", "−", "®–", "®", "—«", "þ«",
                "€«", "¯", "«", "—°ã", "þ°‘", "—°‘", "°‘", "±", "²", "³–",
                "³", "—°", "þ°", "€°", "´", "°", "—µã", "þµ‘", "—µ‘", "µ‘",
                "¶", "ÿ", "¸–", "¸", "—µ", "þµ", "€µ", "¹", "µ", "—îã",
                "þî‘", "—î‘", "î‘", "ï", "ð", "ñ–", "ñ", "—î", "þî", "€î",
                "ò", "î", "—ºã", "þº‘", "—º‘", "º‘", "»", "¼", "½", "¾",
                "—º", "þº", "€º", "¿", "º", "—Àã", "þÀ‘", "—À‘", "À‘", "Á",
                "Â", "Ã", "Ä", "—À", "þÀ", "€À", "Å", "À", "—Æã", "þÆ‘",
                "—Æ‘", "Æ‘", "Ç", "È", "É", "Ê", "—Æ", "þÆ", "€Æ", "Ë",
                "Æ", "—Ìã", "þÌ‘", "—Ì‘", "Ì‘", "Í", "Î", "Ï", "Ð", "—Ì",
                "þÌ", "€Ì", "Ñ", "Ì", "—Òã", "þÒ‘", "—Ò‘", "Ò‘", "Ó", "Ô",
                "Õ–", "Õ", "—Ò", "þÒ", "€Ò", "Ö", "Ò", "—ãã", "þã‘", "—ã‘",
                "ã‘", "ä", "å", "æ", "ç", "—ã", "þã", "€ã", "è", "ã",
                "€×", "—××", "þ×‘", "—×‘", "×‘", "Ø", "Ù", "Ú", "Û", "—×",
                "þ×", "Ü", "×", "—éé", "þé‘", "—é‘", "é‘", "ê", "ë", "ì–",
                "ì", "—é", "þé", "€é", "í", "é", "—ùù", "þù‘", "—ù‘", "ù‘",
                "ù", "ù", "ù”", "ù•", "—ù", "þù", "€ù", "ú", "ù", "—ÝÝ",
                "þÝ‘", "—Ý‘", "Ý‘", "Þ", "ß", "à", "á", "—Ý", "þÝ", "€Ý",
                "â", "Ý", "öåå", "÷åõ", "öåõ", "åõ", "æ", "ç", "åú", "åü",
                "öå", "÷å", "øå", "è", "å", "—õõ", "þõ‘", "—õ‘", "õ‘", "õ",
                "õ", "—õ", "þõ", "€õ", "ö", "õ", "—÷÷", "þ÷‘", "—÷‘", "÷‘",
                "÷", "÷", "—÷", "þ÷", "€÷", "ø", "÷", "", "‚", "„",
                "…", "†", "‡", "ˆ", "‰", "Š", "‹", "Šã", "Œ", "ƒ",
                "ý"};

        String[] TamilReplace = {"க்ஷ", "க்ஷா", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷொ", "க்ஷோ",
                "க்ஷௌ", "க்ஷை", "க்ஷ்", "ஜௌ", "ஜோ", "ஜொ", "ஜா", "ஜி", "ஜி", "ஜீ",
                "ஜு", "ஜூ", "ஜெ", "ஜே", "ஜை", "ஜ்", "ஜ", "கௌ", "கோ", "கொ",
                "கா", "கி", "கீ", "கு", "கூ", "கெ", "கே", "கை", "க்",
                "க", "ஙௌ", "ஙோ", "ஙொ", "ஙா", "ஙி", "ஙீ", "ஙெ", "ஙே", "ஙை",
                "ங்", "ங", "சௌ", "சோ", "சொ", "சா", "சி", "சீ", "சு", "சூ",
                "செ", "சே", "சை", "ச்", "ச", "ச", "ஞௌ", "ஞோ", "ஞொ", "ஞா",
                "ஞி", "ஞீ", "ஞெ", "ஞே", "ஞை", "ஞ்", "ஞ", "டௌ", "டோ", "டொ",
                "டா", "டி", "டீ", "டு", "டூ", "டெ", "டே", "டை", "ட்", "ட",
                "ணௌ", "ணோ", "ணொ", "ணா", "ணி", "ணீ", "ணூ", "ணு", "ணெ", "ணே",
                "ணை", "ண்", "ண", "தௌ", "தோ", "தொ", "தா", "தி", "தீ", "தூ",
                "து", "தெ", "தே", "தை", "த்", "த", "நௌ", "நோ", "நொ", "நா",
                "நி", "நீ", "நூ", "நு", "நெ", "நே", "நை", "ந்", "ந", "னௌ",
                "னோ", "னொ", "னா", "னி", "னீ", "னூ", "னு", "னெ", "னே", "னை",
                "ன்", "ன", "பௌ", "போ", "பொ", "பா", "பி", "பீ", "பு", "பூ",
                "பெ", "பே", "பை", "ப்", "ப", "மௌ", "மோ", "மொ", "மா", "மி",
                "மீ", "மு", "மூ", "மெ", "மே", "மை", "ம்", "ம", "யௌ", "யோ",
                "யொ", "யா", "யி", "யீ", "யு", "யூ", "யெ", "யே", "யை", "ய்",
                "ய", "ரௌ", "ரோ", "ரொ", "ரா", "ரி", "ரீ", "ரு", "ரூ", "ரெ",
                "ரே", "ரை", "ர்", "ர", "லௌ", "லோ", "லொ", "லா", "லி", "லீ",
                "லூ", "லு", "லெ", "லே", "லை", "ல்", "ல", "ளௌ", "ளோ", "ளொ",
                "ளா", "ளி", "ளீ", "ளு", "ளூ", "ளெ", "ளே", "ளை", "ள்", "ள",
                "வை", "வௌ", "வோ", "வொ", "வா", "வி", "வீ", "வு", "வூ", "வெ",
                "வே", "வ்", "வ", "றௌ", "றோ", "றொ", "றா", "றி", "றீ", "றூ",
                "று", "றெ", "றே", "றை", "ற்", "ற", "ஹௌ", "ஹோ", "ஹொ", "ஹா",
                "ஹி", "ஹீ", "ஹு", "ஹூ", "ஹெ", "ஹே", "ஹை", "ஹ்", "ஹ", "ழௌ",
                "ழோ", "ழொ", "ழா", "ழி", "ழீ", "ழு", "ழூ", "ழெ", "ழே", "ழை",
                "ழ்", "ழ", "ஷௌ", "ஷோ", "ஷொ", "ஷா", "ஷி", "ஷீ", "ஷு", "ஷூ",
                "ஷெ", "ஷே", "ஷை", "ஷ்", "ஷ", "ஷௌ", "ஷோ", "ஷொ", "ஷா", "ஷி",
                "ஷீ", "ஷெ", "ஷே", "ஷை", "ஷ்", "ஷ", "ஸௌ", "ஸோ", "ஸொ", "ஸா",
                "ஸி", "ஸீ", "ஸெ", "ஸே", "ஸை", "ஸ்", "ஸ", "அ", "ஆ", "ஈ",
                "உ", "ஊ", "எ", "ஏ", "ஐ", "ஒ", "ஓ", "ஔ", "ஃ", "இ",
                "ஸ்ரீ"};

        int count = 0;

        while (count < TamilText.length) {
            unicodeStr = unicodeStr.replaceAll(TamilText[count], TamilReplace[count]);
            count++;
        }
        return unicodeStr;
    }

    private String convertToTab(String unicodeStr) {
        String[] TamilText = {"þ", "þ£", "þ¤", "þ¦", "þ§", "þ¨", "ªþ", "«þ", "ªþ£", "«þ£",
                "ªþ÷", "¬þ", "þ¢", "ªè÷", "«è£", "ªè£", "è£", "è¤", "è¦", "°",
                "Ã", "ªè", "«è", "¬è", "è¢", "è", "ªé÷", "«é£", "ªé£", "é£",
                "é¤", "é¦", "±", "Ä", "ªé", "«é", "¬é", "é¢", "é", "ªê÷",
                "«ê£", "ªê£", "ê£", "ê¤", "ê¦", "²", "Å", "ªê", "«ê", "¬ê",
                "ê¢", "ê", "ªü÷", "«ü£", "ªü£", "ü£", "ü¤", "ü¦", "ü§", "ü¨",
                "ªü", "«ü", "¬ü", "ü¢", "ü", "ªë÷", "«ë£", "ªë£", "ë£", "ë¤",
                "ë¦", "³", "Æ", "ªë", "«ë", "¬ë", "ë¢", "ë", "ªì÷", "«ì£",
                "ªì£", "ì£", "ì¤", "®", "¯", "ì¦", "´", "Ç", "ªì", "«ì",
                "¬ì", "ì¢", "ì", "ªí÷", "«í£", "ªí£", "í£", "í¤", "í¦", "µ",
                "È", "ªí", "«í", "¬í", "í¢", "í", "ªî÷", "«î£", "ªî£", "î£",
                "î¤", "î¦", "¶", "É", "ªî", "«î", "¬î", "î¢", "î", "ªï÷",
                "«ï£", "ªï£", "ï£", "ï¤", "ï¦", "¸", "Ë", "ªï", "«ï", "¬ï",
                "ï¢", "ï", "ªù÷", "«ù£", "ªù£", "ù£", "ù¤", "ù¦", "Â", "Û",
                "ªù", "«ù", "¬ù", "ù¢", "ù", "ªð÷", "«ð£", "ªð£", "ð£", "ð¤",
                "ð¦", "¹", "Ì", "ªð", "«ð", "¬ð", "ð¢", "ð", "ªñ÷", "«ñ£",
                "ªñ£", "ñ£", "ñ¤", "ñ¦", "º", "Í", "ªñ", "«ñ", "¬ñ", "ñ¢",
                "ñ", "ªò÷", "«ò£", "ªò£", "ò£", "ò¤", "ò¦", "»", "Î", "ªò",
                "«ò", "¬ò", "ò¢", "ò", "ªó÷", "«ó£", "ªó£", "ó£", "ó¤", "ó¦",
                "¼", "Ï", "ªó", "«ó", "¬ó", "ó¢", "ó", "ªô÷", "«ô£", "ªô£",
                "ô£", "ô¤", "ô¦", "½", "Ö", "ªô", "«ô", "¬ô", "ô¢", "ô",
                "ª÷÷", "«÷£", "ª÷£", "÷£", "÷¤", "÷¦", "À", "Ù", "ª÷", "«÷",
                "¬÷", "÷¢", "÷", "ªõ÷", "«õ£", "ªõ£", "õ£", "õ¤", "õ¦", "¾",
                "×", "ªõ", "«õ", "¬õ", "õ¢", "õ", "ªö÷", "«ö£", "ªö£", "ö£",
                "ö¤", "ö¦", "¿", "Ø", "ªö", "«ö", "¬ö", "ö¢", "ö", "ªø÷",
                "«ø£", "ªø£", "ø£", "ø¤", "ø¦", "Á", "Ú", "ªø", "«ø", "¬ø",
                "ø¢", "ø", "ªý÷", "«ý£", "ªý£", "ý£", "ý¤", "ý¦", "ªý", "«ý",
                "¬ý", "ý¢", "ý", "ªû÷", "«û£", "ªû£", "û£", "û¤", "û¦", "ªû",
                "«û", "¬û", "û¢", "û", "ªú÷", "«ú£", "ªú£", "ú£", "ú¤", "ú¦",
                "ªú", "«ú", "¬ú", "ú¢", "ú", "Ü", "Ý", "Þ", "ß", "à",
                "á", "â", "ã", "ä", "å", "æ", "å÷", "ç", "‚", "ƒ",
                "„", "…", "†", "—", "˜", "–", "", "ˆ", "‰", "Ã",
                "ÿ"};

        String[] TamilReplace = {"க்ஷ", "க்ஷா", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷொ", "க்ஷோ",
                "க்ஷௌ", "க்ஷை", "க்ஷ்", "கௌ", "கோ", "கொ", "கா", "கி", "கீ", "கு",
                "கூ", "கெ", "கே", "கை", "க்", "க", "ஙௌ", "ஙோ", "ஙொ", "ஙா",
                "ஙி", "ஙீ", "ஙு", "ஙூ", "ஙெ", "ஙே", "ஙை", "ங்", "ங", "சௌ",
                "சோ", "சொ", "சா", "சி", "சீ", "சு", "சூ", "செ", "சே", "சை",
                "ச்", "ச", "ஜௌ", "ஜோ", "ஜொ", "ஜா", "ஜி", "ஜீ", "ஜு", "ஜூ",
                "ஜெ", "ஜே", "ஜை", "ஜ்", "ஜ", "ஞௌ", "ஞோ", "ஞொ", "ஞா", "ஞி",
                "ஞீ", "ஞு", "ஞூ", "ஞெ", "ஞே", "ஞை", "ஞ்", "ஞ", "டௌ", "டோ",
                "டொ", "டா", "டி", "டி", "டீ", "டீ", "டு", "டூ", "டெ", "டே",
                "டை", "ட்", "ட", "ணௌ", "ணோ", "ணொ", "ணா", "ணி", "ணீ", "ணு",
                "ணூ", "ணெ", "ணே", "ணை", "ண்", "ண", "தௌ", "தோ", "தொ", "தா",
                "தி", "தீ", "து", "தூ", "தெ", "தே", "தை", "த்", "த", "நௌ",
                "நோ", "நொ", "நா", "நி", "நீ", "நு", "நூ", "நெ", "நே", "நை",
                "ந்", "ந", "னௌ", "னோ", "னொ", "னா", "னி", "னீ", "னு", "னூ",
                "னெ", "னே", "னை", "ன்", "ன", "பௌ", "போ", "பொ", "பா", "பி",
                "பீ", "பு", "பூ", "பெ", "பே", "பை", "ப்", "ப", "மௌ", "மோ",
                "மொ", "மா", "மி", "மீ", "மு", "மூ", "மெ", "மே", "மை", "ம்",
                "ம", "யௌ", "யோ", "யொ", "யா", "யி", "யீ", "யு", "யூ", "யெ",
                "யே", "யை", "ய்", "ய", "ரௌ", "ரோ", "ரொ", "ரா", "ரி", "ரீ",
                "ரு", "ரூ", "ரெ", "ரே", "ரை", "ர்", "ர", "லௌ", "லோ", "லொ",
                "லா", "லி", "லீ", "லு", "லூ", "லெ", "லே", "லை", "ல்", "ல",
                "ளௌ", "ளோ", "ளொ", "ளா", "ளி", "ளீ", "ளு", "ளூ", "ளெ", "ளே",
                "ளை", "ள்", "ள", "வௌ", "வோ", "வொ", "வா", "வி", "வீ", "வு",
                "வூ", "வெ", "வே", "வை", "வ்", "வ", "ழௌ", "ழோ", "ழொ", "ழா",
                "ழி", "ழீ", "ழு", "ழூ", "ழெ", "ழே", "ழை", "ழ்", "ழ", "றௌ",
                "றோ", "றொ", "றா", "றி", "றீ", "று", "றூ", "றெ", "றே", "றை",
                "ற்", "ற", "ஹௌ", "ஹோ", "ஹொ", "ஹா", "ஹி", "ஹீ", "ஹெ", "ஹே",
                "ஹை", "ஹ்", "ஹ", "ஷௌ", "ஷோ", "ஷொ", "ஷா", "ஷி", "ஷீ", "ஷெ",
                "ஷே", "ஷை", "ஷ்", "ஷ", "ஸௌ", "ஸோ", "ஸொ", "ஸா", "ஸி", "ஸீ",
                "ஸெ", "ஸே", "ஸை", "ஸ்", "ஸ", "அ", "ஆ", "இ", "ஈ", "உ",
                "ஊ", "எ", "ஏ", "ஐ", "ஒ", "ஓ", "ஔ", "ஃ", "௧", "௨",
                "௩", "௪", "௫", "௰", "௱", "௲", "௯", "௭", "௮", "௬",
                "ஸ்ரீ"};

        int count = 0;
        while (count < TamilText.length) {
            unicodeStr = unicodeStr.replaceAll(TamilText[count], TamilReplace[count]);
            count++;
        }

        return unicodeStr;
    }

    private String convertToTam(String unicodeStr) {
        String[] TamilText = {"þ", "þ£", "¬þ", "V", "r", "þ§", "þ¨", "ªþ", "«þ", "ªþ£",
                "«þ£", "ªþ÷", "z", "ªü÷", "«ü£", "ªü£", "ü£", "T", "p", "ü§",
                "ü¨", "ªü", "«ü", "¬ü", "ªü£", "x", "ü", "ªè÷", "«è£", "ªè£",
                "è£", "A", "W", "°", "Ã", "ªè", "«è", "¬è", "‚", "è",
                "ªé÷", "«é£", "ªé£", "é£", "B", "X", "±", "Ä", "ªé", "«é",
                "¬é", "ƒ", "é", "ªê÷", "«ê£", "ªê£", "ê£", "C", "Y", "²",
                "Å", "ªê", "«ê", "¬ê", "„", "ê", "ªë÷", "«ë£", "ªë£", "ë£",
                "D", "Z", "³", "Æ", "ªë", "«ë", "¬ë", "…", "ë", "ªì÷",
                "«ì£", "ªì£", "ì£", "®", "¯", "´", "Ç", "ªì", "«ì", "¬ì",
                "†", "ì", "ªí÷", "«í£", "ªí£", "í£", "E", "a", "µ", "È",
                "ªí", "«í", "¬í", "‡", "í", "ªî÷", "«î£", "ªî£", "î£", "F",
                "b", "¶", "É", "ªî", "«î", "¬î", "ˆ", "î", "ªï÷", "«ï£",
                "ªï£", "ï£", "G", "c", "¸", "Ë", "ªï", "«ï", "¬ï", "ªï£",
                "‰", "ï", "ªù÷", "«ù£", "ªù£", "ù£", "Q", "m", "Â", "Û",
                "ªù", "«ù", "¬ù", "ªù£", "¡", "ù", "ªð÷", "«ð£", "ªð£", "ð£",
                "H", "d", "¹", "Ì", "ªð", "«ð", "¬ð", "ªð£", "Š", "ð",
                "ªñ÷", "«ñ£", "ªñ£", "ñ£", "I", "e", "º", "Í", "ªñ", "«ñ",
                "¬ñ", "ªñ£", "‹", "ñ", "ªò÷", "«ò£", "ò", "ò£", "J", "f",
                "»", "Î", "ªò", "«ò", "¬ò", "ªò£", "Œ", "ò", "ªó÷", "«ó£",
                "ªó£", "ó£", "K", "g", "¼", "Ï", "ªó", "«ó", "¬ó", "˜",
                "ó", "ªô÷", "«ô£", "ªô£", "ô£", "L", "h", "½", "Ö", "ªô",
                "«ô", "¬ô", "™", "ô", "ª÷÷", "«÷£", "÷", "÷£", "O", "k",
                "À", "Ù", "ª÷", "«÷", "¬÷", "œ", "÷", "ªõ÷", "«õ£", "ªõ£",
                "õ£", "M", "i", "¾", "×", "ªõ", "«õ", "¬õ", "ªõ£", "š",
                "õ", "ªö÷", "«ö£", "ªö£", "ö£", "N", "j", "¿", "Ø", "ªö",
                "«ö", "¬ö", "›", "ö", "ªø÷", "«ø£", "ªø£", "ø£", "P", "l",
                "Á", "Ú", "ªø", "«ø", "¬ø", "ªø£", "Ÿ", "ø", "ªý÷", "«ý£",
                "ªý£", "ý£", "U", "q", "ªý", "«ý", "¬ý", "y", "ý", "ªû÷",
                "«û£", "ªû£", "û£", "S", "o", "ªû", "«û", "¬û", "ªû£", "w",
                "û", "ªú÷", "«ú£", "ªú£", "ú£", "R", "n", "ªú", "«ú", "¬ú",
                "v", "ú", "Ü", "Ý", "Þ", "ß", "à", "á", "â", "ã",
                "ä", "å", "æ", "å÷", "ç", "ÿ", "ƒ"};

        String[] TamilReplace = {"க்ஷ", "க்ஷா", "க்ஷை", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷொ",
                "க்ஷோ", "க்ஷௌ", "க்ஷ்", "ஜௌ", "ஜோ", "ஜொ", "ஜா", "ஜி", "ஜீ", "ஜு",
                "ஜூ", "ஜெ", "ஜே", "ஜை", "ஜொ", "ஜ்", "ஜ", "கௌ", "கோ", "கொ",
                "கா", "கி", "கீ", "கு", "கூ", "கெ", "கே", "கை", "க்", "க",
                "ஙௌ", "ஙோ", "ஙொ", "ஙா", "ஙி", "ஙீ", "ஙு", "ஙூ", "ஙெ", "ஙே",
                "ஙை", "ங்", "ங", "சௌ", "சோ", "சொ", "சா", "சி", "சீ", "சு",
                "சூ", "செ", "சே", "சை", "ச்", "ச", "ஞௌ", "ஞோ", "ஞொ", "ஞா",
                "ஞி", "ஞீ", "ஞு", "ஞூ", "ஞெ", "ஞே", "ஞை", "ஞ்", "ஞ", "டௌ",
                "டோ", "டொ", "டா", "டி", "டீ", "டு", "டூ", "டெ", "டே", "டை",
                "ட்", "ட", "ணௌ", "ணோ", "ணொ", "ணா", "ணி", "ணீ", "ணு", "ணூ",
                "ணெ", "ணே", "ணை", "ண்", "ண", "தௌ", "தோ", "தொ", "தா", "தி",
                "தீ", "து", "தூ", "தெ", "தே", "தை", "த்", "த", "நௌ", "நோ",
                "நொ", "நா", "நி", "நீ", "நு", "நூ", "நெ", "நே", "நை", "நொ",
                "ந்", "ந", "னௌ", "னோ", "னொ", "னா", "னி", "னீ", "னு", "னூ",
                "னெ", "னே", "னை", "னொ", "ன்", "ன", "பௌ", "போ", "பொ", "பா",
                "பி", "பீ", "பு", "பூ", "பெ", "பே", "பை", "பொ", "ப்", "ப",
                "மௌ", "மோ", "மொ", "மா", "மி", "மீ", "மு", "மூ", "மெ", "மே",
                "மை", "மொ", "ம்", "ம", "யௌ", "யோ", "ய", "யா", "யி", "யீ",
                "யு", "யூ", "யெ", "யே", "யை", "யொ", "ய்", "ய", "ரௌ", "ரோ",
                "ரொ", "ரா", "ரி", "ரீ", "ரு", "ரூ", "ரெ", "ரே", "ரை", "ர்",
                "ர", "லௌ", "லோ", "லொ", "லா", "லி", "லீ", "லு", "லூ", "லெ",
                "லே", "லை", "ல்", "ல", "ளௌ", "ளோ", "ள", "ளா", "ளி", "ளீ",
                "ளு", "ளூ", "ளெ", "ளே", "ளை", "ள்", "ள", "வௌ", "வோ", "வொ",
                "வா", "வி", "வீ", "வு", "வூ", "வெ", "வே", "வை", "வொ", "வ்",
                "வ", "ழௌ", "ழோ", "ழொ", "ழா", "ழி", "ழீ", "ழு", "ழூ", "ழெ",
                "ழே", "ழை", "ழ்", "ழ", "றௌ", "றோ", "றொ", "றா", "றி", "றீ",
                "று", "றூ", "றெ", "றே", "றை", "றொ", "ற்", "ற", "ஹௌ", "ஹோ",
                "ஹொ", "ஹா", "ஹி", "ஹீ", "ஹெ", "ஹே", "ஹை", "ஹ்", "ஹ", "ஷௌ",
                "ஷோ", "ஷொ", "ஷா", "ஷி", "ஷீ", "ஷெ", "ஷே", "ஷை", "ஷொ", "ஷ்",
                "ஷ", "ஸௌ", "ஸோ", "ஸொ", "ஸா", "ஸி", "ஸீ", "ஸெ", "ஸே", "ஸை",
                "ஸ்", "ஸ", "அ", "ஆ", "இ", "ஈ", "உ", "ஊ", "எ", "ஏ",
                "ஐ", "ஒ", "ஓ", "ஔ", "ஃ", "ஸ்ரீ", "‘"};

        int count = 0;
        while (count < TamilText.length) {
            unicodeStr = unicodeStr.replaceAll(TamilText[count], TamilReplace[count]);
            count++;
        }
        return unicodeStr;

    }
}
