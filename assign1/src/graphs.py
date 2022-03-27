import matplotlib.pyplot as plt
import numpy as np

x = lambda l : sum(l)/len(l)

#Exercise 1
m_sizes = [600, 1000, 1400, 1800, 2200, 2600, 3000]

cpp_1_time = [[0.248, 0.249, 0.246], [1.335, 1.374, 1.486], [5.128, 5.191, 5.286],[21.140, 21.266, 21.309], [44.456, 49.951, 44.539], [82.563, 84.404, 83.500], [139.442, 138.376, 141.202]]
cpp_1_time_mean = list(map(x, cpp_1_time))

cpp_2_time = [[0.151, 0.152, 0.154], [0.637, 0.629, 0.638], [1.897, 1.917, 1.830],[3.892, 3.900, 3.892], [7.566 , 7.175, 7.178], [12.028, 11.895, 11.899], [19.114, 19.332 , 19.797]]
cpp_2_time_mean = list(map(x, cpp_2_time))

plt.plot(m_sizes, cpp_1_time_mean, label = "Naive Multiplication")
plt.plot(m_sizes, cpp_2_time_mean, label = "Line Multiplication")
plt.xlabel("Matrix Size")
plt.ylabel("Execution Time (s)")
plt.legend()
plt.savefig("graphs/ex1_time.png")
plt.close()

cpp_1_L1DCM = [[244567864, 244444047, 244478967], [1216993178, 1226433575, 1226466093], [3496708772, 3520701252, 3521342910],[9068198210, 9078764278, 9086526668], [17642644537, 17642622097, 17643525589], [30901240812, 30900291855, 30900917727], [50316207768, 50316461057, 50312090830]]
cpp_1_L1DCM_mean = list(map(x, cpp_1_L1DCM))

cpp_2_L1DCM = [[27153282, 27141381, 27284049], [125953821, 126041514, 126031628], [347166511, 347904673, 346710720],[746820416, 745937990, 745898301], [2079608217, 2077201559, 2077393985], [4413044878, 4412951269, 4413186706], [6780645975, 6780836067 , 6780760320]]
cpp_2_L1DCM_mean = list(map(x, cpp_2_L1DCM))

plt.plot(m_sizes, cpp_1_L1DCM_mean, label = "Naive Multiplication")
plt.plot(m_sizes, cpp_2_L1DCM_mean, label = "Line Multiplication")
plt.xlabel("Matrix Size")
plt.ylabel("L1 Data Cache Misses")
plt.legend()
plt.savefig("graphs/ex1_L1DCM.png")
plt.close()

cpp_1_L2DCM = [[39314259, 39020937, 39933474], [188705416, 196642105, 192798322], [720096635, 767861178, 732707506],[4504534434, 5190185403, 4120265003], [22239312462, 21540711253, 22419759033], [50911386405, 50853210377, 51532188583], [94749120913, 95167903575, 93383496102]]
cpp_1_L2DCM_mean = list(map(x, cpp_1_L2DCM))

cpp_2_L2DCM = [[54529367, 54559552, 57544134], [248623757, 258320343, 257570301], [671668621, 689758554, 681399256],[1446189392, 1422612495, 1466887340], [2561609484, 2613662751, 2570689873], [4245737000, 4315073550, 4304612351], [6706541756, 6766952494 , 6794866473]]
cpp_2_L2DCM_mean = list(map(x, cpp_2_L2DCM))

plt.plot(m_sizes, cpp_1_L2DCM_mean, label = "Naive Multiplication")
plt.plot(m_sizes, cpp_2_L2DCM_mean, label = "Line Multiplication")
plt.xlabel("Matrix Size")
plt.ylabel("L2 Data Cache Misses")
plt.legend()
plt.savefig("graphs/ex1_L2DCM.png")
plt.close()

def gflops(time_list, size_list):
    ret = []
    for t,s in zip(time_list, size_list):
        ret.append(2*s**3/t * 1e-9)
    return ret

plt.plot(m_sizes, gflops(cpp_1_time_mean, m_sizes), label = "Naive Multiplication")
plt.plot(m_sizes, gflops(cpp_2_time_mean, m_sizes), label = "Line Multiplication")
plt.xlabel("Matrix Size")
plt.ylabel("Floating Point Operations per Second (gflop/s)")
plt.legend()
plt.savefig("graphs/ex1_gflops.png")
plt.close()

#Exercise 2
java_1_time = [[0.242095, 0.283412, 0.278737], [2.263389, 2.125062, 2.037778], [7.339578, 7.396729, 7.416124],[22.549400, 22.553031, 22.566179], [47.337149, 46.635489, 47.564372], [84.899614, 84.806130, 84.410386], [140.887719, 140.450531, 141.489318]]
java_1_time_mean = list(map(x, java_1_time))

java_2_time = [[0.176985, 0.164318, 0.181724], [0.942510, 0.935275, 0.985335], [2.702358, 2.805933, 2.690271],[5.740973, 5.762401, 5.754250], [10.512228, 10.623340, 10.534425], [17.382626, 18.148559, 17.632482], [28.994091, 28.324997, 29.042478]]
java_2_time_mean = list(map(x, java_2_time))

plt.plot(m_sizes, java_1_time_mean, label = "Naive Multiplication")
plt.plot(m_sizes, java_2_time_mean, label = "Line Multiplication")
plt.xlabel("Matrix Size")
plt.ylabel("Execution Time (s)")
plt.legend()
plt.savefig("graphs/ex2_time.png")
plt.close()

plt.plot(m_sizes, gflops(java_1_time_mean, m_sizes), label = "Naive Multiplication")
plt.plot(m_sizes, gflops(java_2_time_mean, m_sizes), label = "Line Multiplication")
plt.xlabel("Matrix Size")
plt.ylabel("Floating Point Operations per Second (gflop/s)")
plt.legend()
plt.savefig("graphs/ex2_gflops.png")
plt.show()

#Exercise 3
m_sizes_3 = [4096, 6144, 8192, 10240]
cpp_2_time_3 = [[50.802, 52.221, 51.234], [183.496, 183.270, 184.048], [441.876, 447.068, 441.589],[853.970, 855.411, 870.774]]
cpp_2_time_mean_3 = list(map(x, cpp_2_time_3))

cpp_2_L1DCM_3 = [[17627031321, 17663468196, 17655830594], [59600016129, 59578900971, 59607573320], [141259169229, 141260024754, 141236755018],[275802709027, 275868009574, 275826307054]]
cpp_2_L1DCM_mean_3 = list(map(x, cpp_2_L1DCM_3))

cpp_2_L2DCM_3 = [[17386767508, 17540023925, 17408211501], [59955964085, 59993437753, 59908786138], [140611193241, 137620859854, 39413103996],[278205615291, 275234620222, 276909511501]]
cpp_2_L2DCM_mean_3 = list(map(x, cpp_2_L2DCM_3))

cpp_3_time_128 = [[49.519, 50.006, 50.062], [177.960, 184.641, 183.238], [449.120, 451.506, 450.996],[882.110, 877.005, 873.727]]
cpp_3_time_mean_128 = list(map(x, cpp_3_time_128))

cpp_3_L1DCM_128 = [[9854266520, 9853971819, 9851673218], [33282003050, 33159536737, 33140161934], [78664786443, 78854372638, 78221462751],[153624732961, 153765918968, 153806062400]]
cpp_3_L1DCM_mean_128 = list(map(x, cpp_3_L1DCM_128))

cpp_3_L2DCM_128 = [[30771319946, 31200439372, 31576031436], [108868647304, 109849265910, 108726499012], [256953815477, 258019132080, 258676756813],[508267833814, 507479032292, 506135277676]]
cpp_3_L2DCM_mean_128 = list(map(x, cpp_3_L2DCM_128))

cpp_3_time_256 = [[45.570, 46.845, 46.131], [168.270, 166.984, 168.501], [438.957, 443.564, 440.670],[803.002, 814.442, 801.297]]
cpp_3_time_mean_256 = list(map(x, cpp_3_time_256))

cpp_3_L1DCM_256 = [[9131400162, 9130969409, 9131869471], [30795580754, 30798607919, 30837897893], [73135750850, 72956611213, 72948180989],[142716874485, 142757916610, 142752040331]]
cpp_3_L1DCM_mean_256 = list(map(x, cpp_3_L1DCM_256))

cpp_3_L2DCM_256 = [[22486963091, 22953124290, 22674048019], [75282664426, 77255827159, 75160605133], [179719151476, 177722649739, 177683628239],[348436296418, 355153829961, 348493193191]]
cpp_3_L2DCM_mean_256 = list(map(x, cpp_3_L2DCM_256))

cpp_3_time_512 = [[44.233,44.614, 44.349], [184.065, 186.388, 183.917], [468.177,465.885,470.756],[873.059, 857.396, 862.220]]
cpp_3_time_mean_512 = list(map(x, cpp_3_time_512))

cpp_3_L1DCM_512 = [[8754377404, 8755740444,8755114462], [29609932259, 29640661332, 29618093798], [70255011021, 70225748041, 70252668750],[136883271398,136864887773, 136875730920]]
cpp_3_L1DCM_mean_512 = list(map(x, cpp_3_L1DCM_512))

cpp_3_L2DCM_512 = [[18725675590, 19347541673, 18710795579], [63045210466, 65495650841, 63038756240], [154841344303,153781920064,151484889087],[299774035916, 301046194963, 302320450526]]
cpp_3_L2DCM_mean_512 = list(map(x, cpp_3_L2DCM_512))

cpp_3_time_1024 = [[54.174,54.895, 54.843], [189.592, 191.053, 190.056], [469.027,470.852,469.536],[903.137	, 906.935, 906.168]]
cpp_3_time_mean_1024 = list(map(x, cpp_3_time_1024))

cpp_3_L1DCM_1024 = [[8783612716, 8788436324,8782312091], [29700369001, 29708051303,29708634563], [70384815130, 70428404452, 70484408796],[137538226664,	137534007011, 137504205620]]
cpp_3_L1DCM_mean_1024 = list(map(x, cpp_3_L1DCM_1024))

cpp_3_L2DCM_1024 = [[18031890278,18032840409,18406769685], [61806773550, 62951534873, 61837539610], [144524164126, 147388655290,146292039179],[293450920671, 286534408820, 	287059612481]]
cpp_3_L2DCM_mean_1024 = list(map(x, cpp_3_L2DCM_1024))