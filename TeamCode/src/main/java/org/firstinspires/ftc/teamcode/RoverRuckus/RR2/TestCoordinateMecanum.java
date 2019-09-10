package org.firstinspires.ftc.teamcode.RoverRuckus.RR2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RoverRuckus.RR2.Auto.AutoBase;
import org.firstinspires.ftc.teamcode.RoverRuckus.RR2.RR2;
@Deprecated
@Disabled
@Autonomous(name="TestCoordinateMecanum", group="Linear Opmode")
public class TestCoordinateMecanum extends AutoBase {
        RR2 robot;
        @Override
        public void runOpMode() {
            robot = new RR2(hardwareMap,telemetry,this);
            initLogic();
            robot.intializeIMU();
            waitForStart();

            double[][] doubleLoop = {
                    {      0.000000000000,       0.000000000000,       0.000000000000,       0.000000000000},
                    {      2.278133601058,       0.165595619743,       0.246742211234,       0.152215977706},
                    {      3.534600501785,       0.097087078824,       0.351550916754,       0.308971619330},
                    {      4.153721865892,       0.155360851254,       0.433755954065,       0.470370974685},
                    {      4.584443919171,       0.443482309876,       0.504572305681,       0.636485057956},
                    {      4.907176835583,       0.763457057041,       0.568277286400,       0.807356047153},
                    {      5.160623093015,       1.100846871530,       0.627052672868,       0.983001016306},
                    {      5.367945145063,       1.446646620321,       0.682173638429,       1.163415219464},
                    {      5.543826397912,       1.795035298603,       0.734456021930,       1.348574960523},
                    {      5.697636138423,       2.142072511441,       0.784455667336,       1.538440089867},
                    {      5.836169485930,       2.485347589428,       0.832574875296,       1.732956170511},
                    {      5.964326911620,       2.823427981519,       0.879102981371,       1.932056355114},
                    {      6.084719241634,       3.155115586407,       0.924262430587,       2.135663012228},
                    {      6.199750374659,       3.479989430875,       0.968231262464,       2.343689136330},
                    {      6.311195693060,       3.797949865732,       1.011143154113,       2.556039572216},
                    {      6.420002677417,       4.108947380306,       1.053109498295,       2.772612080419},
                    {      6.526945393269,       4.413151905305,       1.094218434224,       2.993298266731},
                    {      6.632643082093,       4.710915908009,       1.134544236091,       3.217984395677},
                    {      6.737280434981,       5.002462566500,       1.174145085138,       3.446552104921},
                    {      6.841057315890,       5.288176272553,       1.213074154483,       3.678879035132},
                    {      6.944011704400,       5.568385733978,       1.251371456811,       3.914839387710},
                    {      7.045992377599,       5.843352407160,       1.289075842859,       4.154304420959},
                    {      7.147192720094,       6.113644994114,       1.326217182353,       4.397142893737},
                    {      7.247377845946,       6.379516669852,       1.362821791138,       4.643221464338},
                    {      7.346306533689,       6.641218770015,       1.398912952649,       4.892405051239},
                    {      7.444367432489,       6.899592900118,       1.434511354268,       5.144557161421},
                    {      7.540820136015,       7.154434926217,       1.469629511255,       5.399540191187},
                    {      7.635591020471,       7.406183938446,       1.504288765179,       5.657215703763},
                    {      7.728704675572,       7.655332995244,       1.538496843303,       5.917444687370},
                    {      7.819512576683,       7.901752131649,       1.572269489210,       6.180087797047},
                    {      7.908522008018,       8.146441159250,       1.605615698523,       6.445005583066},
                    {      7.995189112336,       8.389350250715,       1.638542543858,       6.712058708471},
                    {      8.079048694448,       8.630497659984,       1.671059841000,       6.981108158017},
                    {      8.160210339032,       8.870500792112,       1.703174962845,       7.252015440513},
                    {      8.238519030081,       9.109709075163,       1.734893499109,       7.524642786428},
                    {      8.313451014507,       9.348065697941,       1.766221027535,       7.798853342441},
                    {      8.384656925013,       9.585699981447,       1.797164305978,       8.074511364470},
                    {      8.452642178114,       9.823733559265,       1.827728778269,       8.351482410649},
                    {      8.516553044668,      10.061723688943,       1.857914976787,       8.629633535595},
                    {      8.576246752876,      10.300086457038,       1.887732042428,       8.908833487280},
                    {      8.631778507270,      10.539449856729,       1.917179334773,       9.188952907723},
                    {      8.682272306563,      10.779360391810,       1.946263252149,       9.469864538719},
                    {      8.727340748128,      11.019918290916,       1.974987075604,       9.751443433775},
                    {      8.730187302774,      11.214070871287,       2.003357515816,      10.033567177386},
                    {      8.668566211944,      11.331406942159,       2.031612485211,      10.316116112795},
                    {      8.580687306294,      11.419312694068,       2.059898231867,      10.598973579356},
                    {      8.490774081073,      11.509225919358,       2.088203489992,      10.882026160600},
                    {      8.398585478141,      11.601414522372,       2.116517268344,      11.165163944128},
                    {      8.303876507365,      11.696123493245,       2.144828953372,      11.448280794402},
                    {      8.206410505368,      11.793589495357,       2.173128337885,      11.731274639531},
                    {      8.105960143697,      11.894039857164,       2.201405651242,      12.014047773101},
                    {      8.002308920365,      11.997691080658,       2.229651591139,      12.296507172075},
                    {      7.895253180427,      12.104746820787,       2.257857357106,      12.578564831750},
                    {      7.784604716334,      12.215395285106,       2.286014685799,      12.860138118673},
                    {      7.670194002221,      12.329805999484,       2.314115888168,      13.141150142368},
                    {      7.551874117680,      12.448125884336,       2.342153888587,      13.421530146557},
                    {      7.429525414859,      12.570474587519,       2.370122265978,      13.701213920468},
                    {      7.303060977113,      12.696939025687,       2.398015296990,      13.980144230583},
                    {      7.172432906511,      12.827567096776,       2.425828001229,      14.258271272974},
                    {      7.037639460022,      12.962360543824,       2.453556188537,      14.535553146058},
                    {      6.898733028424,      13.101266976057,       2.481196508259,      14.811956343270},
                    {      6.755828916412,      13.244171088785,       2.508746500402,      15.087456264702},
                    {      6.609114835264,      13.390885170731,       2.536204648559,      15.362037746271},
                    {      6.458860959556,      13.541139047317,       2.563570434370,      15.635695604388},
                    {      6.305430325835,      13.694569681987,       2.590844393273,      15.908435193412},
                    {      6.140229627331,      13.829922829621,       2.618028171176,      16.180272972444},
                    {      5.935397678837,      13.877228943731,       2.645205699915,      16.451237077131},
                    {      5.699782702171,      13.847190838746,       2.672651117561,      16.721367891258},
                    {      5.464424350800,      13.804999080961,       2.700406358764,      16.990718611898},
                    {      5.232968881399,      13.755654148718,       2.728492017096,      17.259355800874},
                    {      5.006476766596,      13.697963073648,       2.756931392592,      17.527359914255},
                    {      4.785797474623,      13.629864357210,       2.785750846798,      17.794825800582},
                    {      4.572331719073,      13.550672691479,       2.814983432211,      18.061863157634},
                    {      4.367344093068,      13.459187030809,       2.844659812141,      18.328596936726},
                    {      4.171740683141,      13.353190960590,       2.874820170192,      18.595167683023},
                    {      3.986622929395,      13.231082648522,       2.905507398548,      18.861731800017},
                    {      3.813098673231,      13.091630173634,       2.936772548939,      19.128461726476},
                    {      3.652160064086,      12.933678532435,       2.968668895440,      19.395546014635},
                    {      3.504424777833,      12.755490514769,       3.001259310177,      19.663189299454},
                    {      3.370402593573,      12.555790522967,       3.034613774464,      19.931612150309},
                    {      3.250344144468,      12.333468522614,       3.068812532396,      20.201050798519},
                    {      3.144264281748,      12.087836043220,       3.103945405756,      20.471756736741},
                    {      3.051829306611,      11.818361473702,       3.140114288936,      20.743996189248},
                    {      2.972345014648,      11.524718297720,       3.177434498153,      21.018049455437},
                    {      2.904491915472,      11.205821099928,       3.216038017874,      21.294210132473},
                    {      2.846935700181,      10.862190761815,       3.256082917795,      21.572784226439},
                    {      2.797978845040,      10.494560395989,       3.297741783135,      21.854089164734},
                    {      2.755082136570,      10.101962160713,       3.341222739639,      22.138452725376},
                    {      2.715696171627,       9.684766080539,       3.386775244484,      22.426211901266},
                    {      2.676995832008,       9.243491915189,       3.434692699501,      22.717711719141},
                    {      2.635559094537,       8.777513890760,       3.485332852639,      23.013304033839},
                    {      2.587615700252,       8.285868779954,       3.539142834370,      23.313346318574},
                    {      2.529023704015,       7.767108607121,       3.596689360472,      23.618200471147},
                    {      2.455000818992,       7.218454852533,       3.658712871450,      23.928231654594},
                    {      2.359797339331,       6.635032771360,       3.726217851131,      24.243807188659},
                    {      2.236221852216,       6.008950779771,       3.800636755257,      24.565295506004},
                    {      2.074401521475,       5.326760147116,       3.884140895507,      24.893065184207},
                    {      1.858617274032,       4.562951135609,       3.980349662916,      25.227484061755},
                    {      1.557558059150,       3.660021768232,       4.096263117955,      25.568918443312},
                    {      0.985719712568,       2.230435521532,       4.249791579777,      25.917732396847},
                    {      0.000000000000,       0.000000000000,       4.627428714281,      26.274287142811},
            };



            while (opModeIsActive()) {


                //robot.splineMove(doubleLoop, 20);

            }
        }
}
