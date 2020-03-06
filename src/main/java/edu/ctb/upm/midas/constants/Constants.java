package edu.ctb.upm.midas.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 27/3/17.
 * @project ExtractionInformationWikipedia
 * @version ${<VERSION>}
 * @author Gerardo Lagunes G.
 * @className Constants
 * @see
 */
@Component
public class Constants {

    @Value("${my.service.client.disease_album.name}")
    public String DISNET_DISALBUM_CLIENT_NAME;
    @Value("${my.service.client.disease_album.url}")
    public String SERVICE_DISALBUM_URL;
    @Value("${my.service.client.disease_album.path.last}")
    public String SERVICE_DISALBUM_PATH_LAST;
    @Value("${my.service.client.disease_album.path.get}")
    public String SERVICE_DISALBUM_PATH_GET;

    @Value("${my.service.disease_album.name}")
    public String SERVICE_DISALBUM_NAME;
    @Value("${my.service.disease_album.code}")
    public String SERVICE_DISALBUM_CODE;
    @Value("${my.service.metamap.name}")
    public String SERVICE_METAMAP_NAME;
    @Value("${my.service.metamap.code}")
    public String SERVICE_METAMAP_CODE;
    @Value("${my.service.tvp.name}")
    public String SERVICE_TVP_NAME;
    @Value("${my.service.tvp.code}")
    public String SERVICE_TVP_CODE;
    @Value("${my.service.wte.name}")
    public String SERVICE_WTE_NAME;
    @Value("${my.service.wte.code}")
    public String SERVICE_WTE_CODE;



    public static final String HTTP_HEADER = "http://";
    public static final String VERSION_PROJECT = "1.0";

    public static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJncmFyZG9sYWdhckBob3RtYWlsLmNvbSIsImF1ZCI6IndlYiIsIm5hbWUiOiJHZXJhcmRvIExhZ3VuZXMiLCJ1c2VyIjp0cnVlLCJpYXQiOjE1MDk2MTQyNjh9.uVhDgfLrAgdnj02Hsbgfj9tkVlfni89i0hKVYW31eHApCHpheikK9ae1MhbzRhiyUcFGMKwtiyVgff5NCMY3PA";
    public static final String EXTRACTION_WIKIPEDIA_FOLDER = "tmp/wikipedia/";
    public static final String PM_RETRIEVAL_HISTORY_FOLDER = "tmp/pubmed/";
    public static final String STATISTICS_HISTORY_FOLDER = "tmp/statistics/";
    public static final String TEST_ANALYSIS_HISTORY_DIRECTORY = "tmp/test_analysis/";
    public static final String ANALYSIS_HISTORY_DIRECTORY = "tmp/analysis/";
    public static final String ANALYSIS_2_HISTORY_DIRECTORY = "tmp/analysis_2/";
    public static final String PM_RETRIEVAL_FILE_NAME = "_pubmed_retrieval";
    public static final String WIKIPEDIA_RESOURCE_FILE_NAME = "_wikipedia_resource";
    public static final String WIKIPEDIA_SEMTYPE_FILE_NAME = "_wikipedia_semtype";
    public static final String WIKIPEDIA_RETRIEVAL_FILE_NAME = "_wikipedia_retrieval";
    public static final String WIKIPEDIA_DISEASE_LIST_FILE_NAME = "_wikipedia_disease_list";
    public static final String ANALISIS_FOLDER = "tmp/analisis/";

    public static final String DOT_JSON = ".json";
    public static final String DOT_XML = ".xml";

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public static final String HTML_REF = "references";
    public static final String HTML_CITE = "cite";

    public static final List<String> ANALYSIS_SNAPSHOT_LIST = new ArrayList<String>(){{
        add("2018-02-01");
    add("2018-02-15");
    add("2018-03-01");
    add("2018-03-15");
    add("2018-04-01");
    add("2018-04-15");
    add("2018-05-01");
    add("2018-05-15");
    add("2018-06-01");
    add("2018-06-15");
    add("2018-07-01");
    add("2018-07-15");
    add("2018-08-01");
    add("2018-08-15");
    add("2018-09-01");
    add("2018-09-15");
    add("2018-10-01");
    add("2018-10-15");
    add("2018-11-01");
    add("2018-11-15");
    add("2018-12-01");
    add("2018-12-15");
    add("2019-01-01");
    add("2019-01-15");
    add("2019-02-01");
    add("2019-02-15");
    add("2019-03-01");
    add("2019-03-15");
    add("2019-04-01");
    add("2019-04-15");
    add("2019-05-01");
    add("2019-05-15");
    add("2019-06-01");
    add("2019-06-15");
    add("2019-07-01");
    add("2019-07-15");}};


    /** Validaciones */
    public static final String SEMANTIC_TYPES[] = { "sosy", "diap", "dsyn", "fndg", "lbpr", "lbtr" };
    public static final List<String> SEMANTIC_TYPES_LIST = new ArrayList<String>(){{
        //<editor-fold desc="old">
        /*
        add("sosy");
        //<editor-fold desc="Description">
        *//*
            DEF: An observable manifestation of a disease or condition based on clinical
judgment, or a manifestation of a disease or condition which is experienced by
the patient and reported as a subjective observation.
            EX: Dyspnea; Nausea; Pain
        *//*
        //</editor-fold>
        add("diap");
        //<editor-fold desc="Description">
        *//*
            DEF: A procedure, method, or technique used to determine the nature or identity
of a disease or disorder.  This excludes procedures which are primarily carried
out on specimens in a laboratory.
            EX: Biopsy; Heart Auscultation; Magnetic Resonance Imaging
        *//*
        //</editor-fold>
        add("dsyn");
        //<editor-fold desc="Description">
        *//*
            DEF: A condition which alters or interferes with a normal process, state, or
activity of an organism.  It is usually characterized by the abnormal functioning
of one or more of the host's systems, parts, or organs.  Included here is a
complex of symptoms descriptive of a disorder.
            EX: Diabetes Mellitus; Drug Allergy; Malabsorption Syndrome
        *//*
        //</editor-fold>
        add("fndg");
        //<editor-fold desc="Description">
        *//*
            DEF: That which is discovered by direct observation or measurement of an
organism attribute or condition, including the clinical history of the patient.
The history of the presence of a disease is a 'Finding' and is distinguished
from the disease itself.
            EX: Birth History; Downward displacement of diaphragm; Decreased glucose level
        *//*
        //</editor-fold>
        add("lbpr");
        //<editor-fold desc="Description">
        *//*
            DEF: A procedure, method, or technique used to determine the composition,
quantity, or concentration of a specimen, and which is carried out in a clinical
laboratory.  Included here are procedures which measure the times and rates of
reactions.
            EX: Blood Protein Electrophoresis; Crystallography; Radioimmunoassay
        *//*
        //</editor-fold>
        add("lbtr");
        //<editor-fold desc="Description">
        *//*
            DEF: The outcome of a specific getPageIdAndTheirSpecificRevisionByTitleAndSnapshot to measure an attribute or to determine the
presence, absence, or degree of a condition.
            EX: Blood Flow Velocity; Serum Calcium Level; Spinal Fluid Pressure
        *//*
        //</editor-fold>
        add("inpr");//todas las siguientes para encontrar enfermedades mentales
        //<editor-fold desc="Description">
        *//*
            DEF: A conceptual entity resulting from human endeavor.  Concepts assigned to
this type generally refer to information created by humans for some purpose.
            EX: Decision Support Techniques; Information Systems; Literature
        *//*
        //</editor-fold>
        add("menp");
        //<editor-fold desc="Description">
        *//*
            DEF: A physiologic function involving the mind or cognitive processing.
            EX: Anger; Auditory Fatigue; Avoidance Learning
        *//*
        //</editor-fold>
        add("mobd");
        //<editor-fold desc="Description">
        *//*
            DEF: A clinically significant dysfunction whose major manifestation is
behavioral or psychological.  These dysfunctions may have identified or presumed
biological etiologies or manifestations.
            EX: Agoraphobia; Cyclothymic Disorder; Frigidity
        *//*
        //</editor-fold>
        add("patf");
        //<editor-fold desc="Description">
        *//*
            DEF: A disordered process, activity, or state of the organism as a whole, of a
body system or systems, or of multiple organs or tissues.  Included here are
normal responses to a negative stimulus as well as patholologic conditions or
states that are less specific than a disease.  Pathologic functions frequently
have systemic effects.
            EX: Inflammation; Shock; Thrombosis
        *//*
        //</editor-fold>
        add("cgab");//para encontrar Coma
        //<editor-fold desc="Description">
        *//*
            DEF: An abnormal structure, or one that is abnormal in size or location, present
at birth or evolving over time as a result of a defect in embryogenesis.
            EX: Albinism; Cleft palate with cleft lip; Polydactyly of toes
        *//*
        //</editor-fold>*/
        //</editor-fold>

        //DISO|Disorders Metamap group
        add("acab");//acab|T020|Acquired Abnormality
        //<editor-fold desc="Description">
        /*
            DEF: An abnormal structure, or one that is abnormal in size or location, found
in or deriving from a previously normal structure.  Acquired abnormalities are
distinguished from diseases even though they may result in pathological
functioning (e.g., "hernias incarcerate").
            EX: Hemorrhoids; Hernia, Femoral; Cauliflower ear
        */
        //</editor-fold>
        add("anab");//anab|T190|Anatomical Abnormality
        //<editor-fold desc="Description">
        /*
            DEF: An abnormal structure, or one that is abnormal in size or location.
            EX: Bronchial Fistula; Foot Deformities; Hyperostosis of skull
        */
        //</editor-fold>
        add("comd");//comd|T049|Cell or Molecular Dysfunction
        //<editor-fold desc="Description">
        /*
            DEF: A pathologic function inherent to cells, parts of cells, or molecules.
            EX: DNA Damage; Wallerian Degeneration; Atypical squamous metaplasia
        */
        //</editor-fold>
        add("cgab");//cgab|T019|Congenital Abnormality (YES)
        //<editor-fold desc="Description">
        /*
            DEF: An abnormal structure, or one that is abnormal in size or location, present
at birth or evolving over time as a result of a defect in embryogenesis.
            EX: Albinism; Cleft palate with cleft lip; Polydactyly of toes
        */
        //</editor-fold>
        add("dsyn");//dsyn|T047|Disease or Syndrome (YES)
        //<editor-fold desc="Description">
        /*
            DEF: A condition which alters or interferes with a normal process, state, or
activity of an organism.  It is usually characterized by the abnormal functioning
of one or more of the host's systems, parts, or organs.  Included here is a
complex of symptoms descriptive of a disorder.
            EX: Diabetes Mellitus; Drug Allergy; Malabsorption Syndrome
        */
        //</editor-fold>
        add("emod");//emod|T050|Experimental Model of Disease
        //<editor-fold desc="Description">
        /*
            DEF: A representation in a non-human organism of a human disease for the purpose
of research into its mechanism or treatment.
            EX: Alloxan Diabetes; Liver Cirrhosis, Experimental; Transient Gene Knock-Out
        */
        //</editor-fold>
        add("fndg");//fndg|T033|Finding (YES)
        //<editor-fold desc="Description">
        /*
            DEF: That which is discovered by direct observation or measurement of an
organism attribute or condition, including the clinical history of the patient.
The history of the presence of a disease is a 'Finding' and is distinguished
from the disease itself.
            EX: Birth History; Downward displacement of diaphragm; Decreased glucose level
        */
        //</editor-fold>
        add("mobd");//mobd|T048|Mental or Behavioral Dysfunction (YES)
        //<editor-fold desc="Description">
        /*
            DEF: A clinically significant dysfunction whose major manifestation is
behavioral or psychological.  These dysfunctions may have identified or presumed
biological etiologies or manifestations.
            EX: Agoraphobia; Cyclothymic Disorder; Frigidity
        */
        //</editor-fold>
        add("neop");//neop|T191|Neoplastic Process
        //<editor-fold desc="Description">
        /*
            DEF: A new and abnormal growth of tissue in which the growth is uncontrolled and
progressive.  The growths may be malignant or benign.
            EX: Abdominal Neoplasms; Bowen's Disease; Polyp in nasopharynx
        */
        //</editor-fold>
        add("patf");//patf|T046|Pathologic Function (YES)
        //<editor-fold desc="Description">
        /*
            DEF: A disordered process, activity, or state of the organism as a whole, of a
body system or systems, or of multiple organs or tissues.  Included here are
normal responses to a negative stimulus as well as patholologic conditions or
states that are less specific than a disease.  Pathologic functions frequently
have systemic effects.
            EX: Inflammation; Shock; Thrombosis
        */
        //</editor-fold>
        add("sosy");//sosy|T184|Sign or Symptom (YES)
        //<editor-fold desc="Description">
        /*
            DEF: An observable manifestation of a disease or condition based on clinical
judgment, or a manifestation of a disease or condition which is experienced by
the patient and reported as a subjective observation.
            EX: Dyspnea; Nausea; Pain
        */
        //</editor-fold>

        //PHEN|Phenomena Metamap group
        add("lbtr");//lbtr|T034|Laboratory or Test Result (YES)
        //<editor-fold desc="Description">
        /*
            DEF: The outcome of a specific getPageIdAndTheirSpecificRevisionByTitleAndSnapshot to measure an attribute or to determine the
presence, absence, or degree of a condition.
            EX: Blood Flow Velocity; Serum Calcium Level; Spinal Fluid Pressure
        */
        //</editor-fold>

        //PHYS|Physiology Metamap group
        add("clna");//clna|T201|Clinical Attribute
        //<editor-fold desc="Description">
        /*
            DEF: An observable or measurable property or state of an organism of clinical
interest.
            EX: Bone Density; heart rate; Range of Motion, Articular
        */
        //</editor-fold>
        add("menp");//menp|T041|Mental Process (YES)
        //<editor-fold desc="Description">
        /*
            DEF: A physiologic function involving the mind or cognitive processing.
            EX: Anger; Auditory Fatigue; Avoidance Learning
        */
        //</editor-fold>

        //PROC|Procedures Metamap group
        add("diap");//diap|T060|Diagnostic Procedure (YES)
        //<editor-fold desc="Description">
        /*
            DEF: A procedure, method, or technique used to determine the nature or identity
of a disease or disorder.  This excludes procedures which are primarily carried
out on specimens in a laboratory.
            EX: Biopsy; Heart Auscultation; Magnetic Resonance Imaging
        */
        //</editor-fold>
        add("lbpr");//lbpr|T059|Laboratory Procedure (YES)
        //<editor-fold desc="Description">
        /*
            DEF: A procedure, method, or technique used to determine the composition,
quantity, or concentration of a specimen, and which is carried out in a clinical
laboratory.  Included here are procedures which measure the times and rates of
reactions.
            EX: Blood Protein Electrophoresis; Crystallography; Radioimmunoassay
        */
        //</editor-fold>

        //CONC|Concepts & Ideas Metamap group
//        add("inpr");//inpr|T170|Intellectual Product (YES)











    }};

    /**
     * Fuentes de extracción
     * */
    public static final String SOURCE_WIKIPEDIA = "wikipedia";
    public static final String SOURCE_WIKIPEDIA_CODE = "SO01";

    public static final String SOURCE_PUBMED = "pubmed";
    public static final String SOURCE_PUBMED_CODE = "SO02";

    public static final String SOURCE_MAYOCLINIC = "mayoclinic";
    public static final String SOURCE_MAYOCLINIC_CODE = "SO03";

    public static final String ABSTRACT_SECTION = "abstract";

    public static final String MESH_RESOURCE_NAME = "MeSH";

    /** Constantes para insertar en la base de datos */

    public static final String CONTENT_TYPE_PARA = "PARA";
    public static final String CONTENT_TYPE_LIST = "LIST";

    /**
     * Constantes para la extracción
     */

    public static final String WIKI_INFOBOX_SECTIONS[] = { "Classification and external resources", "Identifiers", "Databases", "External databases" };

    public static final String HTML_P = "p";
    public static final String HTML_H1 = "h1";
    public static final String HTML_H2 = "h2";
    public static final String HTML_H3 = "h3";
    public static final String HTML_H4 = "h4";
    public static final String HTML_DIV = "div";
    public static final String HTML_TR = "tr";
    public static final String HTML_TH = "th";
    public static final String HTML_TD = "td";
    public static final String HTML_LI = "li";
    public static final String HTML_UL = "ul";
    public static final String HTML_OL = "ol";
    public static final String HTML_DL = "dl";
    public static final String HTML_IMG = "img";
    public static final String HTML_ALT = "alt";
    public static final String HTML_A = "a";
    public static final String HTML_B = "b";
    public static final String HTML_HREF = "href";
    public static final String HTML_COLSPAN = "colspan";
    public static final String HEAD = "head";
    public static final String FOOT = "foot";
    public static final String LIST = "list";
    public static final String TEXT = "text";
    public static final String HTML_TABLE = "table";

    /** CONSULTAS BIBLIOTECA JSOUP */
    public static final String QUERY_A_HREF = "a[href]";
    public static final String QUERY_A_CLASS = "a[class=";
    public static final String QUERY_ABS_HREF = "abs:href";
    public static final String QUERY_TABLE_CLASS = "table[class=";
    public static final String QUERY_DIV_CLASS = "div[class=";
    public static final String QUERY_TD_CLASS = "td[class=";//text-align:center
    public static final String QUERY_TH_STYLE_TEXT_ALIGN_CENTER = "th[style=text-align:center]";
    public static final String QUERY_TD_STYLE_TEXT_ALIGN_CENTER = "td[style=text-align:center]";

    /** LISTA DE ETIQUETAS REFERENCTES A LISTAS HTML */
    public static final List<String> HTML_LIST_TAG_PARENTS_LIST_TAG = new ArrayList<String>(){{
        add("ul");
        add("ol");
        add("dl");
    }};

    /** Ruta del archivo XML source */
    public static final String XML_SOURCE_FILE = "src/main/resources/parameters/sources.xml";
    public static final String TXT_FILE = "src/main/resources/parameters/texts.txt";
    public static final String TXT_CONCEPT_FILE = "src/main/resources/parameters/concept.txt";


    /** Constantes para leer XML */

    public static final String XML_ROOT_TAG = "source";
    public static final String XML_TAG_SECTIONS = "sections";
    public static final String XML_TAG_HIGHLIGTS = "highlights";
    public static final String XML_TAG_LINKS = "links";
    public static final String XML_TAG_NAME = "name";
    public static final String XML_TAG_LINK = "link";
    public static final String XML_ATT_CONSULT = "consult";
    public static final String XML_ATT_CONSULT_YES = "y";
    public static final String XML_ATT_CONSULT_NO = "n";
    public static final String XML_ATT_ID = "id";
    public static final String XML_ATT_TYPE = "type";
    public static final String XML_ATT_CLASS = "class";
    public static final String XML_ATT_NAME = "name";
    public static final String XML_ATT_DESC = "desc";


    /**
     * XML_HIGHLIGHTS HL Constantes para consultar
     */
    public static final String XML_HL_DISEASENAME = "diseasename";
    public static final String XML_HL_INFOBOX = "infobox";
    public static final String XML_HL_EXTERNAL_TEXT = "externalresoruce";
    public static final String XML_HL_PLAIN_LIST = "plainlist";
    public static final String XML_HL_HORIZONTAL_LIST = "horizontallist";
    public static final String XML_HL_TEXT_ALIGN_CENTER = "textaligncenter";


    /**
     * Constantes para la validación de conceptos (metamap y proceso de validación de terminos)
     */
    public static final String METAMAP_FOLDER = "tmp/mm/";
    public static final String METAMAP_FILE_NAME = "_metamap_filter";
    public static final String TVP_RETRIEVAL_HISTORY_FOLDER = "tmp/tvp/";
    public static final String TVP_RETRIEVAL_FILE_NAME = "_tvp_validation";
    public static final String PM_RETRIEVAL_DOCUMENT_FILE_NAME = "_document_pubmed_retrieval";
    public static final String PM_RETRIEVAL_DISEASE_LIST_FILE_NAME = "_disease_list_pubmed_retrieval";


    /**
     * Constantes para la validación de conceptos (metamap y proceso de validación de terminos)
     */

    public static final String CONSULT_SOURCE_ALL = "all";
    public static final String CONSULT_LAST_SNAPSHOT = "last";

    /**
     * Fecha por default
     */
    @SuppressWarnings("deprecation")
    public static final Date DEFAULT_DATE = new Date(70, 0, 1);

    /**
     * Cadena en blancos
     */
    public static final String BLANKS = "";

    /**
     * Punto
     */
    public static final String POINT = ".";

    /**
     * Coma
     */
    public static final String COMMA = ",";

    /**
     * Comillas (quotation marks)
     */
    public static final String QUOTATION_MARKS = "\"";

    /**
     * Punto y coma
     */
    public static final String COMMA_DOT = ";";

    /**
     * Cero
     */
    public static final String ZERO = "0";

    /**
     * Slash
     */
    public static final String SLASH = "/";

    /**
     * Linea del piso
     */
    public static final String UNDER_SCORE = "_";

    /**
     * Guion
     */
    public static final String DASH = "-";

    /**
     * Uno
     */
    public static final String ONE = "1";

    /**
     * Tiempo estandar para dormir y esperar
     */
    public static final long SLEEP_TIME = 15000;

    /**
     * Menos
     */
    public static final String MINUS = "-";
    /**
     * Menos Cero
     */
    public static final String MINUS_ZERO = "-0";

    /**
     * Parametro de Url 1
     */
    public static final String URL_PARAM01 = "&1";

    /**
     * Parametro de Url 2
     */
    public static final String URL_PARAM02 = "&2";

    /**
     * Dos puntos
     */
    public static final String TWO_POINTS = ":";

    /**
     * Dos puntos seguido de cero
     */
    public static final String TWO_POINTS_ZERO = ":0";

    /**
     * Tres
     */
    public static final String THREE = "3";

    /**
     * Dos
     */
    public static final String TWO = "2";

    /**
     * Separador decimal para split
     */
    public static final String DECIM_SEP = "\\.";

    /**
     * Punto decimal
     */
    public static final String DEC_POINT = ".";

    /**
     * Espacio en blancos
     */
    public static final String BLANK_SPACE = " ";

    /**
     * Cinco
     */
    public static final String FIVE = "5";

    /**
     * Cuatro
     */
    public static final String FOUR = "4";

    /**
     * Ceros
     */
    public static final String ZEROS = "00";

    /**
     * Parametro 01
     */
    public static final String PARAM01 = "01";

    /**
     * Parametro 02
     */
    public static final String PARAM02 = "02";

    /**
     * Parametro 03
     */
    public static final String PARAM03 = "03";

    /**
     * Separador interno de parametros
     */
    public static final String INNER_PARAM_SEP = "_";

    /**
     * Flecha
     */
    public static final String ARROW = "==>";

    /**
     * Parentesis izquierdo
     */
    public static final String LEFT_PARENTHESIS = "[";

    /**
     * Parentesis derecho
     */
    public static final String RIGHT_PARENTHESIS = "]";

    /**
     * Porcentaje
     */
    public static final String PERCENTAGE = "%";

    /**
     * Punto
     */
    public static final String DOT = ".";

    /**
     * Add
     */
    public static final String ADD = "ADD";

    /**
     * Change
     */
    public static final String CHANGE = "CHANGE";

    /**
     * Delete
     */
    public static final String DELETE = "DELETE";

    /**
     * Ampersand
     */
    public static final String AMPERSAND = "&";

    /**
     * Init Change
     */
    public static final String INIT_CHANGE = "initChange";

    /**
     * Stdfunreq
     */
    public static final String STDFUNREQ = "stdfunreq";

    /**
     * Init Add
     */
    public static final String INIT_ADD = "initAdd";

    /**
     * Bean de autenticacion
     */
    public static final String AUTENTICATION_BEAN = "autenticationBean";

    /**
     * Nombre del subfile
     */
    public static final String NAME_SUBFILE = "subfile";

    /**
     * Parametro SE01 (Maximo intentoss inicio sesion)
     */
    public static final String SE01 = "SE01";

    /**
     * Parametro SE02 (Registros a mostrar en subfile)
     */
    public static final String SE02 = "SE02";

    /**
     * Parametro SE03 (Paginas en subfile)
     */
    public static final String SE03 = "SE03";

    /**
     * Parametro SE04 (Minimo caracteres contrase?a)
     */
    public static final String SE04 = "SE04";

    /**
     * Parametro SE05 (Dias para cambiar contrase?a)
     */
    public static final String SE05 = "SE05";

    /**
     * Parametro SE06 (Tiempo de bloqueo de usuario minutos)
     */
    public static final String SE06 = "SE06";

    /**
     * Parametro SE07 (Maximo de registros en consulta)
     */
    public static final String SE07 = "SE07";

    /**
     * Parametro SE08 (Direcci?n Endpoint Virtuoso)
     */
    public static final String SE08 = "SE08";

    /**
     * Parametro SE09 (Maximo de registros en autocompletar)
     */
    public static final String SE09 = "SE09";

    /**
     * Parametro SE10 (Url del servicio de visualizacion)
     */
    public static final String SE10 = "SE10";

    /**
     * Parametro SE11 (Indicador de visualizaci?n integrada)
     */
    public static final String SE11 = "SE11";

    /**
     * Nombre de Bean
     */
    public static final Object BEAN_NAME = "beanName";

    /**
     * Timer de tareas automaticas
     */
    public static final String TIMER = "TIMER_";

    /**
     * Seleccione
     */
    public static final String SELECCIONE = "seleccione";

    /**
     * Parametro
     */
    public static final String PARAMETER = "parameter";

    /**
     * Parametro 01 bean
     */
    public static final String BEAN_PARAM01 = "param1";

    /**
     * Parametro 02 bean
     */
    public static final String BEAN_PARAM02 = "param2";

    /**
     * SMTP
     */
    public static final String SMTP = "smtp";

    /**
     * Dias en la semana
     */
    public static final int DAYS_IN_WEEK = 7;

    /**
     * Hora en milisegundos
     */
    public static final long HOUR_MILIS = 3600000 * 23;

    /**
     * Nueva linea
     */
    public static final String NEW_LINE = "\n";

    /**
     * Atributo de Relaci?n
     */
    public static final String REL = "?rel_";

    /**
     * Objeto de query
     */
    public static final String OBJECT = "object";

    /**
     * Menor
     */
    public static final String MINOR = "<";

    /**
     * Mayor
     */
    public static final String MAJOR = ">";

    /**
     * Separador de mail (@)
     */
    public static final String MAIL_SEP = "@";

    /**
     * ID del paginador de listas
     */
    public static final String LIST_PAGINATOR = "listPaginator";

    /**
     * Subfile
     */
    public static final String SUBFILE = "subfile";

    /**
     * Ruta para imagen de hoja
     */
    public static final String PATH_LEAF = "/PhiBaseWeb/xmlhttp/css/rime/css-images/tree_document.gif";

    /**
     * Ruta para imagen de folder open
     */
    public static final String PATH_FOLDER_OPEN = "/PhiBaseWeb/xmlhttp/css/rime/css-images/tree_folder_closed.gif";

    /**
     * Ruta para imagen de folder close
     */
    public static final String PATH_FOLDER_CLOSE = "/PhiBaseWeb/xmlhttp/css/rime/css-images/tree_folder_open.gif";

    /**
     * Nodo Raiz
     */
    public static final String ROOT_NODE = "RootNode";

    /**
     * Ruta icono no encontrado
     */
    public static final String ICON_NOTFOUND = "/PhiBaseWeb/images/btn/vineta1.gif";

    /**
     * Ruta icono encontrado
     */
    public static final String ICON_FOUND = "/PhiBaseWeb/images/icons/normal/16/check_mark_16.png";

    /**
     * Query Result
     */
    public static final Object QUERY_RESULT = "queryResult";

    /**
     * Query Value
     */
    public static final Object QUERY_VALUE = "queryValue";

    /**
     * Gene
     */
    public static final String K00 = "00";

    /**
     * Host
     */
    public static final String K01 = "01";

    /**
     * Pathogen
     */
    public static final String K02 = "02";

    /**
     * Protocol Description
     */
    public static final String K03 = "03";

    /**
     * Interaction Context Mutant
     */
    public static final String K04 = "04";

    /**
     * Disease name
     */
    public static final String K05 = "05";

    /**
     * Gene Locus Id
     */
    public static final String K06 = "06";

    /**
     * Gene Function
     */
    public static final String K07 = "07";

    /**
     * Gene Name
     */
    public static final String K08 = "08";

    /**
     * Allele
     */
    public static final String K09 = "09";

    /**
     * Invitro Growth
     */
    public static final String K10 = "10";

    /**
     * Lethal Knockout
     */
    public static final String K11 = "11";

    /**
     * Gene Accession
     */
    public static final String K12 = "12";

    /**
     * Citation
     */
    public static final String K13 = "13";

    /**
     * InterOperator
     */
    public static final String INTER_OP = "InterOp:";

    /**
     * Link
     */
    public static final String LINK = "link";

    /**
     * Igual
     */
    public static final String EQUAL = "=";

    /**
     * Dollar
     */
    public static final String DOLLAR = "$";

    /**
     * Separador de parametros
     */
    public static final String PARAM_SEP = "<>";

    /**
     * Separador de parametros
     */
    public static final String PARAM_SEP_2 = "?";


    public final static String CHAR_SEPARATOR = "!";

    public final static String BLANK_SPACE_CODE = "%20";

    /**
     * Wikipedia API
     */
    public final static String SQUARE_BRACKETS_OPEN = "[";
    public final static String SQUARE_BRACKETS_CLOSE = "]";

    public final static String QUERY_ELEMENT_NAME = "query";

    public final static String PAGES_ELEMENT_NAME = "pages";
    public final static String PAGES_ELEMENT_PAGEID_NAME = "pageid";
    public final static String PAGES_ELEMENT_TITLE_NAME = "title";

    public final static String REVISIONS_ELEMENT_NAME = "revisions";
    public final static String REVISIONS_ELEMENT_REVID_NAME = "revid";
    public final static String REVISIONS_ELEMENT_PARENTID_NAME = "parentid";
    public final static String REVISIONS_ELEMENT_MINOR_NAME = "minor";
    public final static String REVISIONS_ELEMENT_USER_NAME = "user";
    public final static String REVISIONS_ELEMENT_USERID_NAME = "userid";
    public final static String REVISIONS_ELEMENT_TIMESTAMP_NAME = "timestamp";
    public final static String REVISIONS_ELEMENT_SIZE_NAME = "size";
    public final static String REVISIONS_ELEMENT_COMMENT_NAME = "comment";
    public final static String REVISIONS_ELEMENT_TEXT_NAME = "text";

    public final static String REDIRECTS_ELEMENT_NAME = "redirects";


    /**
     * Rutas
     */
    public final static String VALIDATED_FOLDER = "cnv_data/filter/validated/";
    public final static String NO_VALIDATED_FOLDER = "cnv_data/filter/not_validated/";
    public final static String VALIDATION_FINDINGS_FILE = "vte_data/results/allFindings.fd";
    public final static String DISEASES_FINDINGS_FOLDER = "cnv_data/diseases_findings/";
    public final static String DISEASES_URLS_FILE = "cnv_data/diseases.lst";
    public final static String DISEASES_TEXTS_FOLDER = "cnv_data/diseasesData/";
    public final static String VALIDATION_FINDINGS_TEMP_FOLDER = "vte_data/temp_findings/";


    public final static String ERR_NO_PARAMETER = "No parameter was sent";
    public final static String ERR_EMPTY_PARAMETER = "Empty parameter";



}
