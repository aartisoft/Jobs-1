package nithra.jobs.career.placement.utills;

/**
 * 
 * @author மயூரேசன்
 * @குறிப்பு யுனிக்கோட் முறையில் இருக்கும் சொற்கள் வசனங்களை பாமினி, திஸ்கி, டாப், டாம் போன்ற 
 * குறிமுறைகளுக்கு மாற்றியமைக்க இந்த செயலி உங்களுக்கு உதவும்
 *
 */
public class TamilUtil {
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
	 
	public final static int interchagecustom = 5; 
 
	 
 
	
	/**
	 * இந்த method இன் மூலம் நீங்கள் யுனிக்கோட் குறிமுறையில் இருந்து உங்களுக்குத் 
	 * தேவையான குறிமுறைக்கு சொற்கள், வசனங்களை மாற்றிக் கொள்ளலாம்.
	 * @param encodCode - எந்தக் குறிமுறைக்கு மாற்ற விரும்புகின்றீர்கள் என்பதை இங்கே குறிப்பிட வேண்டும்.
	 * உதாரணமாக பாமினி முறை என்றால் TamilUtil.BAMINI என்றோ அல்லது திஸ்கி முறைமை என்றால் TamilUtil.TSCII
	 * என்றோ குறிப்பிடலாம்.
	 * 
	 * @param unicodeStr யுனிக்கோட் சொற்கள் வசனங்கள்
	 * @return யுனிக்கோடில் இருந்து நீங்கள் குறிப்பிட்ட குறிமுறைக்கு மாற்றப்பட்ட சொற்கள், வசனங்கள் உங்களுக்கு வந்து கிடைக்கும்.
	 */
	public static String convertToTamil(int encodCode, String unicodeStr){
		String convertedStr = "";
		TamilUtil tu = new TamilUtil();
		if(encodCode== TamilUtil.BAMINI){
			convertedStr = tu.convertToBamini(unicodeStr);
		}
		else if(encodCode== TamilUtil.TSCII){
			convertedStr = tu.convertToTSCII(unicodeStr);
		}
		else if(encodCode==TamilUtil.ANJAL){
			convertedStr = tu.convertToAnjal(unicodeStr);
		}
		else if(encodCode==TamilUtil.TAB){
			convertedStr = tu.convertToTab(unicodeStr);
		}
		else if(encodCode==TamilUtil.TAM){
			convertedStr = tu.convertToTam(unicodeStr);
		}
 		else if(encodCode==TamilUtil.interchagecustom){
			convertedStr = tu.convertTointerchagecustom(unicodeStr);
		}
 		 
		return convertedStr;
	}
	private String convertTointerchagecustom(String unicodeStr) {
		unicodeStr = unicodeStr.replace("fi", "if");
		unicodeStr = unicodeStr.replace("qi", "iq");
		unicodeStr = unicodeStr.replace("ri", "ir");
		unicodeStr = unicodeStr.replace("Qi", "iQ");
		unicodeStr = unicodeStr.replace("li", "il");
		unicodeStr = unicodeStr.replace("zi", "iz");
		unicodeStr = unicodeStr.replace("ji", "ij");
		unicodeStr = unicodeStr.replace("ei", "ie");
		unicodeStr = unicodeStr.replace("gi", "ig");
		unicodeStr = unicodeStr.replace("ki", "ik");
		unicodeStr = unicodeStr.replace("ai", "ia");
		unicodeStr = unicodeStr.replace("ui", "iu");
		unicodeStr = unicodeStr.replace("yi", "iy");
		unicodeStr = unicodeStr.replace("ti", "it");
		unicodeStr = unicodeStr.replace("oi", "io");
		unicodeStr = unicodeStr.replace("si", "is");
		unicodeStr = unicodeStr.replace("wi", "iw");
		unicodeStr = unicodeStr.replace("di", "id");
		
		unicodeStr = unicodeStr.replace("[i", "i[");
		unicodeStr = unicodeStr.replace("\\i", "i\\");
		unicodeStr = unicodeStr.replace("]i", "i]");
		unicodeStr = unicodeStr.replace("`i", "i`");
		
		
		
		unicodeStr = unicodeStr.replace("fn", "nf");
		unicodeStr = unicodeStr.replace("qn", "nq");
		unicodeStr = unicodeStr.replace("rn", "nr");
		unicodeStr = unicodeStr.replace("Qn", "nQ");
		unicodeStr = unicodeStr.replace("ln", "nl");
		unicodeStr = unicodeStr.replace("zn", "nz");
		unicodeStr = unicodeStr.replace("jn", "nj");
		unicodeStr = unicodeStr.replace("en", "ne");
		unicodeStr = unicodeStr.replace("gn", "ng");
		unicodeStr = unicodeStr.replace("kn", "nk");
		unicodeStr = unicodeStr.replace("an", "na");
		unicodeStr = unicodeStr.replace("un", "nu");
		unicodeStr = unicodeStr.replace("yn", "ny");
		unicodeStr = unicodeStr.replace("tn", "nt");
		unicodeStr = unicodeStr.replace("on", "no");
		unicodeStr = unicodeStr.replace("sn", "ns");
		unicodeStr = unicodeStr.replace("wn", "nw");
		unicodeStr = unicodeStr.replace("dn", "nd");
		
		unicodeStr = unicodeStr.replace("[n", "n[");
		unicodeStr = unicodeStr.replace("\\n", "n\\");
		unicodeStr = unicodeStr.replace("]n", "n]");
		unicodeStr = unicodeStr.replace("`n", "n`");
		
		unicodeStr = unicodeStr.replace("fN", "Nf");
		unicodeStr = unicodeStr.replace("qN", "Nq");
		unicodeStr = unicodeStr.replace("rN", "Nr");
		unicodeStr = unicodeStr.replace("QN", "NQ");
		unicodeStr = unicodeStr.replace("lN", "Nl");
		unicodeStr = unicodeStr.replace("zN", "Nz");
		unicodeStr = unicodeStr.replace("jN", "Nj");
		unicodeStr = unicodeStr.replace("eN", "Ne");
		unicodeStr = unicodeStr.replace("gN", "Ng");
		unicodeStr = unicodeStr.replace("kN", "Nk");
		unicodeStr = unicodeStr.replace("aN", "Na");
		unicodeStr = unicodeStr.replace("uN", "Nu");
		unicodeStr = unicodeStr.replace("yN", "Ny");
		unicodeStr = unicodeStr.replace("tN", "Nt");
		unicodeStr = unicodeStr.replace("oN", "No");
		unicodeStr = unicodeStr.replace("sN", "Ns");
		unicodeStr = unicodeStr.replace("wN", "Nw");
		unicodeStr = unicodeStr.replace("dN", "Nd");
		
		unicodeStr = unicodeStr.replace("[N", "N[");
		unicodeStr = unicodeStr.replace("\\N", "N\\");
		unicodeStr = unicodeStr.replace("]N", "N]");
		unicodeStr = unicodeStr.replace("`N", "N`");
		
		
		unicodeStr = unicodeStr.replace("fnh", "nfh");
		unicodeStr = unicodeStr.replace("qnh", "nqh");
		unicodeStr = unicodeStr.replace("rnh", "nrh");
		unicodeStr = unicodeStr.replace("Qnh", "nQh");
		unicodeStr = unicodeStr.replace("lnh", "nlh");
		unicodeStr = unicodeStr.replace("znh", "nzh");
		unicodeStr = unicodeStr.replace("jnh", "njh");
		unicodeStr = unicodeStr.replace("enh", "neh");
		unicodeStr = unicodeStr.replace("gnh", "ngh");
		unicodeStr = unicodeStr.replace("knh", "nkh");
		unicodeStr = unicodeStr.replace("anh", "nah");
		unicodeStr = unicodeStr.replace("unh", "nuh");
		unicodeStr = unicodeStr.replace("ynh", "nyh");
		unicodeStr = unicodeStr.replace("tnh", "nth");
		unicodeStr = unicodeStr.replace("onh", "noh");
		unicodeStr = unicodeStr.replace("snh", "nsh");
		unicodeStr = unicodeStr.replace("wnh", "nwh");
		unicodeStr = unicodeStr.replace("dnh", "ndh");
		
		unicodeStr = unicodeStr.replace("[nh", "n[h");
		unicodeStr = unicodeStr.replace("\\nh", "n\\h");
		unicodeStr = unicodeStr.replace("]nh", "n]h");
		unicodeStr = unicodeStr.replace("`nh", "n`h");
		
		unicodeStr = unicodeStr.replace("fNh", "Nfh");
		unicodeStr = unicodeStr.replace("qNh", "Nqh");
		unicodeStr = unicodeStr.replace("rNh", "Nrh");
		unicodeStr = unicodeStr.replace("QNh", "NQh");
		unicodeStr = unicodeStr.replace("lNh", "Nlh");
		unicodeStr = unicodeStr.replace("zNh", "Nzh");
		unicodeStr = unicodeStr.replace("jNh", "Njh");
		unicodeStr = unicodeStr.replace("eNh", "Neh");
		unicodeStr = unicodeStr.replace("gNh", "Ngh");
		unicodeStr = unicodeStr.replace("kNh", "Nkh");
		unicodeStr = unicodeStr.replace("aNh", "Nah");
		unicodeStr = unicodeStr.replace("uNh", "Nuh");
		unicodeStr = unicodeStr.replace("yNh", "Nyh");
		unicodeStr = unicodeStr.replace("tNh", "Nth");
		unicodeStr = unicodeStr.replace("oNh", "Noh");
		unicodeStr = unicodeStr.replace("sNh", "Nsh");
		unicodeStr = unicodeStr.replace("wNh", "Nwh");
		unicodeStr = unicodeStr.replace("dNh", "Ndh");
		
		unicodeStr = unicodeStr.replace("[Nh", "N[h");
		unicodeStr = unicodeStr.replace("\\Nh", "N\\h");
		unicodeStr = unicodeStr.replace("]Nh", "N]h");
		unicodeStr = unicodeStr.replace("`Nh", "N`h");
		
		
		unicodeStr = unicodeStr.replace("fns", "nfs");
		unicodeStr = unicodeStr.replace("qns", "nqs");
		unicodeStr = unicodeStr.replace("rns", "nrs");
		unicodeStr = unicodeStr.replace("Qns", "nQs");
		unicodeStr = unicodeStr.replace("lns", "nls");
		unicodeStr = unicodeStr.replace("zns", "nzs");
		unicodeStr = unicodeStr.replace("jns", "njs");
		unicodeStr = unicodeStr.replace("ens", "nes");
		unicodeStr = unicodeStr.replace("gns", "ngs");
		unicodeStr = unicodeStr.replace("kns", "nks");
		unicodeStr = unicodeStr.replace("ans", "nas");
		unicodeStr = unicodeStr.replace("uns", "nus");
		unicodeStr = unicodeStr.replace("yns", "nys");
		unicodeStr = unicodeStr.replace("tns", "nts");
		unicodeStr = unicodeStr.replace("ons", "nos");
		unicodeStr = unicodeStr.replace("sns", "nss");
		unicodeStr = unicodeStr.replace("wns", "nws");
		unicodeStr = unicodeStr.replace("dns", "nds");
		
		unicodeStr = unicodeStr.replace("[ns", "n[s");
		unicodeStr = unicodeStr.replace("\\ns", "n\\s");
		unicodeStr = unicodeStr.replace("]ns", "n]s");
		unicodeStr = unicodeStr.replace("`ns", "n`s");
		
		return unicodeStr;
		
	}
	
	 
	 
	private String convertToBamini(String unicodeStr){
		unicodeStr = unicodeStr.replace("ஸ்ரீ", "=");
		unicodeStr = unicodeStr.replace(",", ">");
		unicodeStr = unicodeStr.replace("ஜௌ", "n[s");
		unicodeStr = unicodeStr.replace("ஜோ", "N[h");
		unicodeStr = unicodeStr.replace("ஜொ", "n[h");
		unicodeStr = unicodeStr.replace("ஜா", "[h");
		unicodeStr = unicodeStr.replace("ஜி", "[p");
		unicodeStr = unicodeStr.replace("ஜீ", "[P");
		unicodeStr = unicodeStr.replace("ஜு", "[{");
		unicodeStr = unicodeStr.replace("ஜூ", "[_");
		unicodeStr = unicodeStr.replace("ஜெ", "n[");
		unicodeStr = unicodeStr.replace("ஜே", "N[");
		unicodeStr = unicodeStr.replace("ஜை", "i[");
		unicodeStr = unicodeStr.replace("ஜ்", "[;");
		unicodeStr = unicodeStr.replace("ஜ", "[");
		unicodeStr = unicodeStr.replace("கௌ", "nfs");
		unicodeStr = unicodeStr.replace("கோ", "Nfh");
		unicodeStr = unicodeStr.replace("கொ", "nfh");
		unicodeStr = unicodeStr.replace("கா", "fh");
		unicodeStr = unicodeStr.replace("கி", "fp");
		unicodeStr = unicodeStr.replace("கீ", "fP");
		unicodeStr = unicodeStr.replace("கு", "F");
		unicodeStr = unicodeStr.replace("கூ", "$");
		unicodeStr = unicodeStr.replace("கெ", "nf");
		unicodeStr = unicodeStr.replace("கே", "Nf");
		unicodeStr = unicodeStr.replace("கை", "if");
		unicodeStr = unicodeStr.replace("க்", "f;");
		unicodeStr = unicodeStr.replace("க", "f");
		unicodeStr = unicodeStr.replace("ஙௌ", "nqs");
		unicodeStr = unicodeStr.replace("ஙோ", "Nqh");
		unicodeStr = unicodeStr.replace("ஙொ", "nqh");
		unicodeStr = unicodeStr.replace("ஙா", "qh");
		unicodeStr = unicodeStr.replace("ஙி", "qp");
		unicodeStr = unicodeStr.replace("ஙீ", "qP");
		unicodeStr = unicodeStr.replace("ஙு", "q{");
		unicodeStr = unicodeStr.replace("ஙூ", "q+");
		unicodeStr = unicodeStr.replace("ஙெ", "nq");
		unicodeStr = unicodeStr.replace("ஙே", "Nq");
		unicodeStr = unicodeStr.replace("ஙை", "iq");
		unicodeStr = unicodeStr.replace("ங்", "q;");
		unicodeStr = unicodeStr.replace("ங", "q");
		unicodeStr = unicodeStr.replace("சௌ", "nrs");
		unicodeStr = unicodeStr.replace("சோ", "Nrh");
		unicodeStr = unicodeStr.replace("சொ", "nrh");
		unicodeStr = unicodeStr.replace("சா", "rh");
		unicodeStr = unicodeStr.replace("சி", "rp");
		unicodeStr = unicodeStr.replace("சீ", "rP");
		unicodeStr = unicodeStr.replace("சு", "R");
		unicodeStr = unicodeStr.replace("சூ", "R+");
		unicodeStr = unicodeStr.replace("செ", "nr");
		unicodeStr = unicodeStr.replace("சே", "Nr");
		unicodeStr = unicodeStr.replace("சை", "ir");
		unicodeStr = unicodeStr.replace("ச்", "r;");
		unicodeStr = unicodeStr.replace("ச", "r");
		unicodeStr = unicodeStr.replace("ஞௌ", "nQs");
		unicodeStr = unicodeStr.replace("ஞோ", "NQh");
		unicodeStr = unicodeStr.replace("ஞொ", "nQh");
		unicodeStr = unicodeStr.replace("ஞா", "Qh");
		unicodeStr = unicodeStr.replace("ஞி", "Qp");
		unicodeStr = unicodeStr.replace("ஞீ", "QP");
		unicodeStr = unicodeStr.replace("ஞு", "Q{");
		unicodeStr = unicodeStr.replace("ஞூ", "Q{h");
		unicodeStr = unicodeStr.replace("ஞெ", "nQ");
		unicodeStr = unicodeStr.replace("ஞே", "NQ");
		unicodeStr = unicodeStr.replace("ஞை", "iQ");
		unicodeStr = unicodeStr.replace("ஞ்", "Q;");
		unicodeStr = unicodeStr.replace("ஞ", "Q");
		unicodeStr = unicodeStr.replace("டௌ", "nls");
		unicodeStr = unicodeStr.replace("டோ", "Nlh");
		unicodeStr = unicodeStr.replace("டொ", "nlh");
		unicodeStr = unicodeStr.replace("டா", "lh");
		unicodeStr = unicodeStr.replace("டி", "b");
		unicodeStr = unicodeStr.replace("டீ", "B");
		unicodeStr = unicodeStr.replace("டு", "L");
		unicodeStr = unicodeStr.replace("டூ", "^");
		unicodeStr = unicodeStr.replace("டெ", "nl");
		unicodeStr = unicodeStr.replace("டே", "Nl");
		unicodeStr = unicodeStr.replace("டை", "il");
		unicodeStr = unicodeStr.replace("ட்", "l;");
		unicodeStr = unicodeStr.replace("ட", "l");
		unicodeStr = unicodeStr.replace("ணௌ", "nzs");
		unicodeStr = unicodeStr.replace("ணோ", "Nzh");
		unicodeStr = unicodeStr.replace("ணொ", "nzh");
		unicodeStr = unicodeStr.replace("ணா", "zh");
		unicodeStr = unicodeStr.replace("ணி", "zp");
		unicodeStr = unicodeStr.replace("ணீ", "zP");
		unicodeStr = unicodeStr.replace("ணு", "Z");
		unicodeStr = unicodeStr.replace("ணூ", "Z}");
		unicodeStr = unicodeStr.replace("ணெ", "nz");
		unicodeStr = unicodeStr.replace("ணே", "Nz");
		unicodeStr = unicodeStr.replace("ணை", "iz");
		unicodeStr = unicodeStr.replace("ண்", "z;");
		unicodeStr = unicodeStr.replace("ண", "z");
		unicodeStr = unicodeStr.replace("தௌ", "njs");
		unicodeStr = unicodeStr.replace("தோ", "Njh");
		unicodeStr = unicodeStr.replace("தொ", "njh");
		unicodeStr = unicodeStr.replace("தா", "jh");
		unicodeStr = unicodeStr.replace("தி", "jp");
		unicodeStr = unicodeStr.replace("தீ", "jP");
		unicodeStr = unicodeStr.replace("து", "J");
		unicodeStr = unicodeStr.replace("தூ", "J}");
		unicodeStr = unicodeStr.replace("தெ", "nj");
		unicodeStr = unicodeStr.replace("தே", "Nj");
		unicodeStr = unicodeStr.replace("தை", "ij");
		unicodeStr = unicodeStr.replace("த்", "j;");
		unicodeStr = unicodeStr.replace("த", "j");
		unicodeStr = unicodeStr.replace("நௌ", "nes");
		unicodeStr = unicodeStr.replace("நோ", "Neh");
		unicodeStr = unicodeStr.replace("நொ", "neh");
		unicodeStr = unicodeStr.replace("நா", "eh");
		unicodeStr = unicodeStr.replace("நி", "ep");
		unicodeStr = unicodeStr.replace("நீ", "eP");
		unicodeStr = unicodeStr.replace("நு", "E");
		unicodeStr = unicodeStr.replace("நூ", "E}");
		unicodeStr = unicodeStr.replace("நெ", "ne");
		unicodeStr = unicodeStr.replace("நே", "Ne");
		unicodeStr = unicodeStr.replace("நை", "ie");
		unicodeStr = unicodeStr.replace("ந்", "e;");
		unicodeStr = unicodeStr.replace("ந", "e");
		unicodeStr = unicodeStr.replace("னௌ", "nds");
		unicodeStr = unicodeStr.replace("னோ", "Ndh");
		unicodeStr = unicodeStr.replace("னொ", "ndh");
		unicodeStr = unicodeStr.replace("னா", "dh");
		unicodeStr = unicodeStr.replace("னி", "dp");
		unicodeStr = unicodeStr.replace("னீ", "dP");
		unicodeStr = unicodeStr.replace("னு", "D");
		unicodeStr = unicodeStr.replace("னூ", "D}");
		unicodeStr = unicodeStr.replace("னெ", "nd");
		unicodeStr = unicodeStr.replace("னே", "Nd");
		unicodeStr = unicodeStr.replace("னை", "id");
		unicodeStr = unicodeStr.replace("ன்", "d;");
		unicodeStr = unicodeStr.replace("ன", "d");
		unicodeStr = unicodeStr.replace("பௌ", "ngs");
		unicodeStr = unicodeStr.replace("போ", "Ngh");
		unicodeStr = unicodeStr.replace("பொ", "ngh");
		unicodeStr = unicodeStr.replace("பா", "gh");
		unicodeStr = unicodeStr.replace("பி", "gp");
		unicodeStr = unicodeStr.replace("பீ", "gP");
		unicodeStr = unicodeStr.replace("பு", "G");
		unicodeStr = unicodeStr.replace("பூ", "G+");
		unicodeStr = unicodeStr.replace("பெ", "ng");
		unicodeStr = unicodeStr.replace("பே", "Ng");
		unicodeStr = unicodeStr.replace("பை", "ig");
		unicodeStr = unicodeStr.replace("ப்", "g;");
		unicodeStr = unicodeStr.replace("ப", "g");
		unicodeStr = unicodeStr.replace("மௌ", "nks");
		unicodeStr = unicodeStr.replace("மோ", "Nkh");
		unicodeStr = unicodeStr.replace("மொ", "nkh");
		unicodeStr = unicodeStr.replace("மா", "kh");
		unicodeStr = unicodeStr.replace("மி", "kp");
		unicodeStr = unicodeStr.replace("மீ", "kP");
		unicodeStr = unicodeStr.replace("மு", "K");
		unicodeStr = unicodeStr.replace("மூ", "%");
		unicodeStr = unicodeStr.replace("மெ", "nk");
		unicodeStr = unicodeStr.replace("மே", "Nk");
		unicodeStr = unicodeStr.replace("மை", "ik");
		unicodeStr = unicodeStr.replace("ம்", "k;");
		unicodeStr = unicodeStr.replace("ம", "k");
		unicodeStr = unicodeStr.replace("யௌ", "nas");
		unicodeStr = unicodeStr.replace("யோ", "Nah");
		unicodeStr = unicodeStr.replace("யொ", "nah");
		unicodeStr = unicodeStr.replace("யா", "ah");
		unicodeStr = unicodeStr.replace("யி", "ap");
		unicodeStr = unicodeStr.replace("யீ", "aP");
		unicodeStr = unicodeStr.replace("யு", "A");
		unicodeStr = unicodeStr.replace("யூ", "A+");
		unicodeStr = unicodeStr.replace("யெ", "na");
		unicodeStr = unicodeStr.replace("யே", "Na");
		unicodeStr = unicodeStr.replace("யை", "ia");
		unicodeStr = unicodeStr.replace("ய்", "a;");
		unicodeStr = unicodeStr.replace("ய", "a");
		unicodeStr = unicodeStr.replace("ரௌ", "nus");
		unicodeStr = unicodeStr.replace("ரோ", "Nuh");
		unicodeStr = unicodeStr.replace("ரொ", "nuh");
		unicodeStr = unicodeStr.replace("ரா", "uh");
		unicodeStr = unicodeStr.replace("ரி", "up");
		unicodeStr = unicodeStr.replace("ரீ", "uP");
		unicodeStr = unicodeStr.replace("ரு", "U");
		unicodeStr = unicodeStr.replace("ரூ", "\\&");
		unicodeStr = unicodeStr.replace("ரெ", "nu");
		unicodeStr = unicodeStr.replace("ரே", "Nu");
		unicodeStr = unicodeStr.replace("ரை", "iu");
		unicodeStr = unicodeStr.replace("ர்", "u;");
		unicodeStr = unicodeStr.replace("ர", "u");
		unicodeStr = unicodeStr.replace("லௌ", "nys");
		unicodeStr = unicodeStr.replace("லோ", "Nyh");
		unicodeStr = unicodeStr.replace("லொ", "nyh");
		unicodeStr = unicodeStr.replace("லா", "yh");
		unicodeStr = unicodeStr.replace("லி", "yp");
		unicodeStr = unicodeStr.replace("லீ", "yP");
		unicodeStr = unicodeStr.replace("லு", "Y");
		unicodeStr = unicodeStr.replace("லூ", "Y}");
		unicodeStr = unicodeStr.replace("லெ", "ny");
		unicodeStr = unicodeStr.replace("லே", "Ny");
		unicodeStr = unicodeStr.replace("லை", "iy");
		unicodeStr = unicodeStr.replace("ல்", "y;");
		unicodeStr = unicodeStr.replace("ல", "y");
		unicodeStr = unicodeStr.replace("ளௌ", "nss");
		unicodeStr = unicodeStr.replace("ளோ", "Nsh");
		unicodeStr = unicodeStr.replace("ளொ", "nsh");
		unicodeStr = unicodeStr.replace("ளா", "sh");
		unicodeStr = unicodeStr.replace("ளி", "sp");
		unicodeStr = unicodeStr.replace("ளீ", "sP");
		unicodeStr = unicodeStr.replace("ளு", "S");
		unicodeStr = unicodeStr.replace("ளூ", "Sh");
		unicodeStr = unicodeStr.replace("ளெ", "ns");
		unicodeStr = unicodeStr.replace("ளே", "Ns");
		unicodeStr = unicodeStr.replace("ளை", "is");
		unicodeStr = unicodeStr.replace("ள்", "s;");
		unicodeStr = unicodeStr.replace("ள", "s");
		unicodeStr = unicodeStr.replace("வௌ", "nts");
		unicodeStr = unicodeStr.replace("வோ", "Nth");
		unicodeStr = unicodeStr.replace("வொ", "nth");
		unicodeStr = unicodeStr.replace("வா", "th");
		unicodeStr = unicodeStr.replace("வி", "tp");
		unicodeStr = unicodeStr.replace("வீ", "tP");
		unicodeStr = unicodeStr.replace("வு", "T");
		unicodeStr = unicodeStr.replace("வூ", "T+");
		unicodeStr = unicodeStr.replace("வெ", "nt");
		unicodeStr = unicodeStr.replace("வே", "Nt");
		unicodeStr = unicodeStr.replace("வை", "it");
		unicodeStr = unicodeStr.replace("வ்", "t;");
		unicodeStr = unicodeStr.replace("வ", "t");
		unicodeStr = unicodeStr.replace("ழௌ", "nos");
		unicodeStr = unicodeStr.replace("ழோ", "Noh");
		unicodeStr = unicodeStr.replace("ழொ", "noh");
		unicodeStr = unicodeStr.replace("ழா", "oh");
		unicodeStr = unicodeStr.replace("ழி", "op");
		unicodeStr = unicodeStr.replace("ழீ", "oP");
		unicodeStr = unicodeStr.replace("ழு", "O");
		unicodeStr = unicodeStr.replace("ழூ", "*");
		unicodeStr = unicodeStr.replace("ழெ", "no");
		unicodeStr = unicodeStr.replace("ழே", "No");
		unicodeStr = unicodeStr.replace("ழை", "io");
		unicodeStr = unicodeStr.replace("ழ்", "o;");
		unicodeStr = unicodeStr.replace("ழ", "o");
		unicodeStr = unicodeStr.replace("றௌ", "nws");
		unicodeStr = unicodeStr.replace("றோ", "Nwh");
		unicodeStr = unicodeStr.replace("றொ", "nwh");
		unicodeStr = unicodeStr.replace("றா", "wh");
		unicodeStr = unicodeStr.replace("றி", "wp");
		unicodeStr = unicodeStr.replace("றீ", "wP");
		unicodeStr = unicodeStr.replace("று", "W");
		unicodeStr = unicodeStr.replace("றூ", "W}");
		unicodeStr = unicodeStr.replace("றெ", "nw");
		unicodeStr = unicodeStr.replace("றே", "Nw");
		unicodeStr = unicodeStr.replace("றை", "iw");
		unicodeStr = unicodeStr.replace("ற்", "w;");
		unicodeStr = unicodeStr.replace("ற", "w");
		unicodeStr = unicodeStr.replace("ஹௌ", "n`s");
		unicodeStr = unicodeStr.replace("ஹோ", "N`h");
		unicodeStr = unicodeStr.replace("ஹொ", "n`h");
		unicodeStr = unicodeStr.replace("ஹா", "`h");
		unicodeStr = unicodeStr.replace("ஹி", "`p");
		unicodeStr = unicodeStr.replace("ஹீ", "`P");
		unicodeStr = unicodeStr.replace("ஹு", "{`");
		unicodeStr = unicodeStr.replace("ஹூ", "`_");
		unicodeStr = unicodeStr.replace("ஹெ", "n`");
		unicodeStr = unicodeStr.replace("ஹே", "N`");
		unicodeStr = unicodeStr.replace("ஹை", "i`");
		unicodeStr = unicodeStr.replace("ஹ்", "`;");
		unicodeStr = unicodeStr.replace("ஹ", "`");
		unicodeStr = unicodeStr.replace("ஷௌ", "n\\s");
		unicodeStr = unicodeStr.replace("ஷோ", "N\\h");
		unicodeStr = unicodeStr.replace("ஷொ", "n\\h");
		unicodeStr = unicodeStr.replace("ஷா", "\\h");
		unicodeStr = unicodeStr.replace("ஷி", "\\p");
		unicodeStr = unicodeStr.replace("ஷீ", "\\P");
		unicodeStr = unicodeStr.replace("ஷு", "\\{");
		unicodeStr = unicodeStr.replace("ஷூ", "\\\\_");
		unicodeStr = unicodeStr.replace("ஷெ", "n\\");
		unicodeStr = unicodeStr.replace("ஷே", "N\\");
		unicodeStr = unicodeStr.replace("ஷை", "i\\");
		unicodeStr = unicodeStr.replace("ஷ்", "\\;");
		unicodeStr = unicodeStr.replace("ஷ", "\\");
		unicodeStr = unicodeStr.replace("ஸௌ", "n]s");
		unicodeStr = unicodeStr.replace("ஸோ", "N]h");
		unicodeStr = unicodeStr.replace("ஸொ", "n]h");
		unicodeStr = unicodeStr.replace("ஸா", "]h");
		unicodeStr = unicodeStr.replace("ஸி", "]p");
		unicodeStr = unicodeStr.replace("ஸீ", "]P");
		unicodeStr = unicodeStr.replace("ஸு", "]{");
		unicodeStr = unicodeStr.replace("ஸூ", "]_");
		unicodeStr = unicodeStr.replace("ஸெ", "n]");
		unicodeStr = unicodeStr.replace("ஸே", "N]");
		unicodeStr = unicodeStr.replace("ஸை", "i]");
		unicodeStr = unicodeStr.replace("ஸ்", "];");
		unicodeStr = unicodeStr.replace("ஸ", "]");
		unicodeStr = unicodeStr.replace("அ", "m");
		unicodeStr = unicodeStr.replace("ஆ", "M");
		unicodeStr = unicodeStr.replace("இ", ",");
		unicodeStr = unicodeStr.replace("ஈ", "<");
		unicodeStr = unicodeStr.replace("உ", "c");
		unicodeStr = unicodeStr.replace("ஊ", "C");
		unicodeStr = unicodeStr.replace("எ", "v");
		unicodeStr = unicodeStr.replace("ஏ", "V");
		unicodeStr = unicodeStr.replace("ஐ", "I");
		unicodeStr = unicodeStr.replace("ஒ", "x");
		unicodeStr = unicodeStr.replace("ஓ", "X");
		unicodeStr = unicodeStr.replace("ஔ", "xs");
		unicodeStr = unicodeStr.replace("ஃ", "/");
		
		return unicodeStr;
	}
	
	private String convertToTSCII(String unicodeStr){
		String TamilText[] = {
				"·","«","¬","þ","®","¯","°","±","²","³","´","µ","¶",
				
				"ì","¸","¸¡","¸¢","¸£","Ì","Ü","¦¸","§¸","¨¸","¦¸¡","§¸¡","¦¸ª",	
				"í","¹","¹¡","¹¢","¹£","™","›","¦¹","§¹","¨¹","¦¹¡","§¹¡","¦¹ª",	
				"î","º","º¡","º¢","º£","Í","Ý","¦º","§º","¨º","¦º¡","§º¡","¦ºª",	
				"ï","»","»¡","»¢","»£","š","œ","¦»","§»","¨»","¦»¡","§»¡","¦»ª",	
				"ð","¼","¼¡"," Ê","Ë","Î","Þ","¦¼","§¼","¨¼","¦¼¡","§¼¡","¦¼ª",	
				"ñ","½","½¡","½¢","	½£","Ï","ß","¦½","§½","¨½","¦½¡","§½¡","¦½ª",	
				"ò","¾","¾¡","¾¢","¾£","Ð","à","¦¾","§¾","¨¾","¦¾¡","§¾¡","¦¾ª",
				"ó","¿","¿¡","¿¢","¿£","Ñ","á","¦¿","§¿","¨¿","¦¿¡","§¿¡","¦¿ª",	
				"ô","À","À¡","À¢","À£","Ò","â","¦À","§À","¨À","¦À¡","§À¡","¦Àª",	
				"õ","Á","Á¡","Á¢","Á£","Ó","ã","¦Á","§Á","¨Á","¦Á¡","§Á¡","¦Áª",	
				"ö","Â","Â¡","Â¢","Â£","Ô","ä","¦Â","§Â","¨Â","¦Â¡","§Â¡","¦Âª",	
				"÷","Ã","Ã¡","Ã¢","Ã£","Õ","å","¦Ã","§Ã","¨Ã","¦Ã¡","§Ã¡","¦Ãª",	
				"ø","Ä","Ä¡","Ä¢","Ä£","Ö","æ","¦Ä","§Ä","¨Ä","¦Ä¡","§Ä¡","¦Äª",	
				"ù","Å","Å¡","Å¢","Å£","×","ç","¦Å","§Å","¨Å","¦Å¡","§Å¡","¦Åª",	
				"ú","Æ","Æ¡","Æ¢","Æ£","Ø","è","¦Æ","§Æ","¨Æ","¦Æ¡","§Æ¡","¦Æª",	
				"û","Ç","Ç¡","Ç¢","Ç£","Ù","é","¦Ç","§Ç","¨Ç","¦Ç¡","§Ç¡","¦Çª",	
				"ü","È","È¡","È¢","È£","Ú","ê","¦È","§È","¨È","¦È¡","§È¡","¦Èª",	
				"ý","É","É¡","É¢","É£","Û","ë","¦É","§É","¨É","¦É¡","§É¡","¦Éª",	
																	
				"ˆ","ƒ","ƒ¡","ƒ¢","ƒ£","ƒ¤","ƒ¥","¦ƒ","§ƒ","¨ƒ","¦ƒ¡","§ƒ¡","¦ƒª",	
				"‰","„","„¡","„¢","„£","„¤","„¥","¦„","§„","¨„","¦„¡","§„¡","¦„ª",
				"Š","…","…¡","…¢","…£","…¤","…¥","¦…","§…","¨…","¦…¡","§…¡","¦…ª",	
				"‹","†","†¡","†¢","†£","†¤","†¥","¦†","§†","¨†","¦†¡","§†¡","¦†ª",	
				"Œ","‡","‡¡","‡¢","‡£","‡¤","‡¥","¦‡","§‡","¨‡","¦‡¡","§‡¡","¦‡ª"	

		};
		
		String TamilReplace[] = {
				"ஃ","அ","ஆ","இ","ஈ","உ","ஊ","	எ","ஏ","ஐ","ஒ","ஓ","ஔ",	
				
				"க்","க","கா","கி","கீ","கு","கூ","கெ","கே","கை","கொ","கோ","கௌ",	
				"ங்","ங","ஙா","ஙி","ஙீ","ஙு","ஙூ","ஙெ","ஙே","ஙை","ஙொ","ஙோ","ஙௌ",
				"ச்","ச","சா","சி","சீ","சு","சூ","செ","சே","சை","சொ","	சோ","சௌ",
				"ஞ்","ஞ","ஞா","	ஞி","ஞீ","ஞு","ஞூ","ஞெ","ஞே","ஞை","ஞொ","ஞோ","ஞௌ",
				"ட்","ட","டா","டி","டீ","டு","	டூ","டெ","டே","டை","டொ","டோ","டௌ	",
				"ண்","ண","ணா","ணி","ணீ","ணு","ணூ","ணெ","ணே","ணை","ணொ","ணோ","ணௌ",	
				"த்","த","தா","தி","தீ","து","தூ","தெ","தே","தை","தொ","தோ","தௌ",
				"ந்","ந","நா","நி","நீ","நு","நூ","நெ","நே","நை","நொ","நோ","நௌ",
				"ப்","ப","பா","பி","பீ","பு","பூ","பெ","பே","பை","பொ","	போ","பௌ",
				"ம்","ம","மா","மி","	மீ","மு","மூ","மெ","மே","மை","மொ","மோ","மௌ",
				"ய்","ய","யா","யி","	யீ","யு","யூ","யெ","யே","	யை","யொ","யோ","யௌ",
				"ர்","ர","ரா","ரி","ரீ","ரு","ரூ","ரெ","	ரே","ரை","ரொ","ரோ","ரௌ",
				"ல்","ல","லா","லி","	லீ","லு","லூ","லெ","	லே","லை","லொ","லோ","லௌ",
				"வ்","வ","வா","வி","	வீ","வு","வூ","வெ","வே","வை","வொ","வோ","வௌ",
				"ழ்","ழ","ழா","ழி","	ழீ","ழு","ழூ","ழெ","ழே","ழை","ழொ","ழோ","ழௌ",
				"ள்","ள","ளா","ளி","ளீ","ளு","ளூ","ளெ","ளே","ளை","ளொ","ளோ","ளௌ",
				"ற்","ற","றா","றி","றீ","று","றூ","றெ","றே","றை","றொ","றோ","றௌ",
				"ன்","ன","னா","னி","னீ","னு","னூ","னெ","னே","னை","னொ	","னோ","னௌ",
																	
				"ஜ்","ஜ","ஜா","ஜி","ஜீ","ஜு","ஜூ","ஜெ","ஜே","ஜை","ஜொ","ஜோ","ஜௌ",
				"ஷ்","ஷ","ஷா","ஷி","ஷீ","ஷு","	ஷூ","ஷெ","	ஷே","ஷை","ஷொ","ஷோ","ஷௌ",	
				"ஸ்","ஸ","ஸா","ஸி","ஸீ","ஸு","ஸூ","ஸெ","ஸே","ஸை","ஸொ","ஸோ","ஸௌ",	
				"ஹ்","ஹ","ஹா","ஹி","ஹீ","ஹு","ஹூ","ஹெ","ஹே","ஹை","ஹொ","ஹோ","ஹௌ",	
				"க்ஷ்","க்ஷ","	க்ஷா","க்ஷி","க்ஷீ","க்ஷு","க்ஷூ","க்ஷெ","க்ஷே","க்ஷை","க்ஷொ","க்ஷோ","க்ஷௌ"	

		};

		int count = 0;

		while (count < TamilText.length) {
			unicodeStr = unicodeStr.replaceAll(TamilReplace[count],TamilText[count]);
			count++;
		}
		return unicodeStr;
	}
	
	private String convertToAnjal(String unicodeStr){
		String TamilText[] = { "û", "û‘", "û’", "û“", "û”", "û•", "—û", "þû", "—û‘", "÷ñõ",
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

		String TamilReplace[] = {"க்ஷ", "க்ஷா", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷொ", "க்ஷோ",
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
			unicodeStr = unicodeStr.replaceAll(TamilReplace[count],TamilText[count]);
			count++;
		}
		return unicodeStr; 
	}
	
	
	private String convertToTab(String unicodeStr){
		String TamilText[] = {"þ", "þ£", "þ¤", "þ¦", "þ§", "þ¨", "ªþ", "«þ", "ªþ£", "«þ£",
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

		String TamilReplace[] = {"க்ஷ", "க்ஷா", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷொ", "க்ஷோ",
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
			unicodeStr = unicodeStr.replaceAll(TamilReplace[count],TamilText[count]);
			count++;
		}

		return unicodeStr;
	}
	
	private String convertToTam(String unicodeStr){
		String TamilText[] = {"þ", "þ£", "¬þ", "V", "r", "þ§", "þ¨", "ªþ", "«þ", "ªþ£",
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

		String TamilReplace[] = {"க்ஷ", "க்ஷா", "க்ஷை", "க்ஷி", "க்ஷீ", "க்ஷு", "க்ஷூ", "க்ஷெ", "க்ஷே", "க்ஷொ",
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
			unicodeStr = unicodeStr.replaceAll(TamilReplace[count], TamilText[count]);
			count++;
		}
		return unicodeStr;
		
	}
}
