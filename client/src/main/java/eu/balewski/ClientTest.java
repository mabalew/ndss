package eu.balewski;

public class ClientTest {
	static String sgid;

	public static void main(String ... args) {
		if (args == null || args.length < 2) {
			System.out.println("niepoprawna liczba parametrów (-1)");
			System.exit(-1);
		} else {
			if ("-a".equals(args[0])) {
				if (args.length >= 3) {
					sgid = args[3];
					testAdd(args[1], args[2]);
				} else {
					System.out.println("niepoprawna liczba parametrów (-2)");
					System.exit(-2);
				}
			} else if ("-am".equals(args[0])) {
				if (args.length >= 3) {
					sgid = args[3];
					testAddMass(args[1], args[2]);
				} else {
					System.out.println("niepoprawna liczba parametrów (-22)");
					System.out.println(args.length);
					System.exit(-3);
				}
			} else if ("-g".equals(args[0])) {
				if (args.length >= 2) {
					sgid = args[2];
					testGet(args[1]);
				} else {
					System.out.println("niepoprawna liczba parametrów (-3)");
					System.out.println(args.length);
					System.exit(-3);
				}
			} else if ("-l".equals(args[0])) {
        System.out.println("listing");
				if (args.length >= 2) {
          System.out.println("sgid: " + args[1]);
					sgid = args[1];
					testList();
				} else {
					System.out.println("niepoprawna liczba parametrów (-3)");
					System.out.println(args.length);
					System.exit(-3);
				}
			} else if ("-u".equals(args[0])) {
				if (args.length >= 3) {
					sgid = args[3];
					testUpdate(args[1], args[2]);
				} else {
					System.out.println("niepoprawna liczba parametrów (-4)");
					System.exit(-4);
				}
			} else if ("-d".equals(args[0])) {
				if (args.length >= 2) {
					sgid = args[2];
					testDelete(args[1]);
				} else {
					System.out.println("niepoprawna liczba parametrów (-5)");
					System.exit(-5);
				}
			} else if ("-dm".equals(args[0])) {
				if (args.length >= 3) {
					sgid = args[3];
					testDeleteMass(args[1], args[2]);
				} else {
					System.out.println("niepoprawna liczba parametrów (-5)");
					System.exit(-52);
				}
			}
		}
	}

	private static void testAdd(String name, String value) {
		NDSClient client = new NDSClient("add", name, value, sgid);
		System.out.println(client.call());
	}

	private static void testAddMass(String prefix, String count) {
		Integer counter = null;
		try {
			counter = Integer.parseInt(count);
			for (int i = 0; i < counter; i++) {
				testAdd(prefix + i, prefix + i + "_value");
			}
		} catch (Exception e) {
			System.err.println("podana wartość nie jest liczbą");
		}
	}

	private static void testGet(String name) {
		NDSClient client = new NDSClient("get", name, sgid);
		System.out.println(client.call());
	}

	private static void testUpdate(String name, String value) {
		NDSClient client = new NDSClient("update", name, value, sgid);
		System.out.println(client.call());
	}

	private static void testDelete(String name) {
		NDSClient client = new NDSClient("delete", name, sgid);
		System.out.println(client.call());
	}

	private static void testList() {
		NDSClient client = new NDSClient("list", null, sgid);
		System.out.println(client.call());
	}

	private static void testDeleteMass(String prefix, String count) {
		Integer counter = null;
		try {
			counter = Integer.parseInt(count);
			for (int i = 0; i < counter; i++) {
				testDelete(prefix + i);
			}
		} catch (Exception e) {
			System.err.println("podana wartość nie jest liczbą");
		}
	}

}
