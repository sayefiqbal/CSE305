import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author Sayef Iqbal
 *
 */

public class interpreter {

	// private final String :false: = ":false:";
	private static Stack<String> _store = new Stack<String>();
	private static HashMap<String, HashMap<String, String>> _bind = new HashMap<String, HashMap<String, String>>();
	private static ArrayList<Stack<String>> _lStack = new ArrayList<Stack<String>>();
	private static ArrayList<HashMap<String, String>> _lBind = new ArrayList<HashMap<String, String>>();
//	private static ArrayList<HashMap<String, String>> _lClone = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, ArrayList<String>>> _lFun = new ArrayList<HashMap<String, ArrayList<String>>>();
	private static boolean inFun = false; 
	private static boolean inLet = false; 
	private static int YO = 0; 

	public static void interpreter(String input, String output) {
		ArrayList<String> inputList = new ArrayList<String>();
		_lStack.add(new Stack<String>());
		_lBind.add(new HashMap<String, String>());
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String temp;
			while ((temp = reader.readLine()) != null) {
				inputList.add(temp);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			for (int i = 0; i < inputList.size(); ++i) {
				interpret(inputList.get(i));
			}
			while (!_lStack.get(_lStack.size() - 1).isEmpty()) {
				// if(_store.peek()=="quit"){
				// return;
				// }
				String toPush = _lStack.get(_lStack.size() - 1).pop();
				// if (toPush.contains("\"")) {
				// toPush = toPush.substring(1, toPush.length() - 1) + "\n";
				// }else {
				// toPush += "\n";
				// }
				String[] val = toPush.split("\"");
				String sVal = "";
				for (String x : val) {
					sVal += x;
				}
				writer.write(sVal + "\n");
			}
			writer.close();
			System.out.println("It worked");
			;
			;
			;
			;
			return;

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private static void interpret(String command) {
		if (command.contains("quit")) {
			return;
		}
		// if(!_lStack.get(_lStack.size()-1).isEmpty() && _store.peek()==":error:"){
		// _lStack.get(_lStack.size()-1).push(":error:");
		// return;
		// }
		if(inFun) {
			if(command.equals("funEnd")) {
				_bind.put(_lStack.get(_lStack.size()-1).peek(), (HashMap<String, String>) _lBind.get(_lBind.size()-1).clone());
				System.out.println(_lStack.get(_lStack.size()-1).peek());
				_lStack.get(_lStack.size()-2).push(":unit:");
//				System.out.println(_lStack.get(_lStack.size()-1));
				_lStack.remove(_lStack.get(_lStack.size()-1));
				inFun = false;
				YO = _lBind.size()-1;
				return; 
			}else {
				ArrayList<String> temp = _lFun.get(_lFun.size()-1).get(_lStack.get(_lStack.size()-1).peek());
//				System.out.println(_lStack.get(_lStack.size()-1));
				temp.add(command);
//				_lFun.get(_lFun.size()-1).put(_lStack.get(_lStack.size()-1).peek(), temp);
				return; 
			}
		} else if (command.contains("fun ")) {
			String[] v = command.split(" ");
			inFun = true;
			if(inLet) {
				_store.push(v[1]);
			}
//			System.out.println(v);/
			try {
				if(_lFun.get(_lStack.size()-1)==null) {
				
				}
				ArrayList<String> method = new ArrayList<String>();
				method.add(v[2]);
				_lFun.get(_lFun.size()-1).put(v[1], method);
				Stack<String> toP = new Stack<String>();
				toP.push(v[1]);
				_lStack.add(toP);
//				System.out.println("HERE");
			}catch(Exception c) {
				_lFun.add(new HashMap<String,ArrayList<String>>());
				ArrayList<String> method = new ArrayList<String>();
				method.add(v[2]);
				_lFun.get(_lFun.size()-1).put(v[1], method);
				Stack<String> toP = new Stack<String>();
				toP.push(v[1]);
				_lStack.add(toP);
			} 
		} else if (command.contains("push")) {
			String numPush = command.substring(5, command.length());
			try {
				if (numPush.contains("-0")) {
					_lStack.get(_lStack.size()-1).push("0");
					return;
				} else if (numPush.charAt(0) == '-' && !isInt(numPush.substring(1))) {
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				} else if (isInt(numPush)) {
					_lStack.get(_lStack.size()-1).push(numPush);
					return;
					// }else if (numPush.contains("\"")){
					// _lStack.get(_lStack.size()-1).push(numPush.substring(1, numPush.length()-1));
					// return;
				} else {
					_lStack.get(_lStack.size()-1).push(numPush);
					return;
				}
			} catch (Exception ex) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			}
		} else if (command.contains("pop")) {
			try {
				_lStack.get(_lStack.size()-1).pop();
				return;
			} catch (Exception ex) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			}
		} else if (command.contains("add")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				try {
//					System.out.println("Val 1: " + _lBind.get(_lBind.size()-1).get(sVal1));
//					System.out.println("Val 2: " + (_lBind.get(_lBind.size()-1).get(sVal2)));
					if (isName(sVal1) && isName(sVal2)) { // both values are names
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
								&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val1 + val2;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(sVal2);
							int val = val1 + val2;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
							int val1 = Integer.parseInt(sVal1);
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val1 + val2;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isInt(sVal1) && isInt(sVal2)) { // neither value are names
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val1 + val2;
						String sVal = Integer.toString(val);
						_lStack.get(_lStack.size()-1).push(sVal);
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} catch (Exception ex) {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("mul")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				try {
					if (isName(sVal1) && isName(sVal2)) { // both values are names
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
								&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val1 * val2;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(sVal2);
							int val = val1 * val2;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
							int val1 = Integer.parseInt(sVal1);
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val1 * val2;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isInt(sVal1) && isInt(sVal2)) {
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val1 * val2;
						String sVal = Integer.toString(val);
						_lStack.get(_lStack.size()-1).push(sVal);
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} catch (Exception ex) {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("sub")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				try {
					if (isName(sVal1) && isName(sVal2)) { // both values are names
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
								&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val2 - val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(sVal2);
							int val = val2 - val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
							int val1 = Integer.parseInt(sVal1);
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val2 - val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isInt(sVal1) && isInt(sVal2)) {
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val2 - val1;
						String sVal = Integer.toString(val);
						_lStack.get(_lStack.size()-1).push(sVal);
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} catch (Exception ex) {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("div")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				try {
					if (isName(sVal1) && isName(sVal2)) { // both values are names
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
								&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val2 / val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(sVal2);
							int val = val2 / val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
							int val1 = Integer.parseInt(sVal1);
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val2 / val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isInt(sVal1) && isInt(sVal2)) {
						int val1 = Integer.parseInt(sVal1);
						if (val1 == 0) {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
						int val2 = Integer.parseInt(sVal2);
						int val = val2 / val1;
						String sVal = Integer.toString(val);
						_lStack.get(_lStack.size()-1).push(sVal);
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} catch (Exception ex) {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("rem")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				try {
					if (isName(sVal1) && isName(sVal2)) { // both values are names
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
								&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val2 % val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val2 = Integer.parseInt(sVal2);
							int val = val2 % val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
						if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
							int val1 = Integer.parseInt(sVal1);
							int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
							int val = val2 % val1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isInt(sVal1) && isInt(sVal2)) {
						int val1 = Integer.parseInt(sVal1);
						if (val1 == 0) {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
						int val2 = Integer.parseInt(sVal2);
						int val = val2 % val1;
						String sVal = Integer.toString(val);
						_lStack.get(_lStack.size()-1).push(sVal);
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} catch (Exception ex) {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("neg")) {
			if (_lStack.get(_lStack.size()-1).isEmpty()) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				try {
					if (isName(sVal1)) { // both values are names
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))) {
							int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
							int val = val1 * -1;
							String sVal = Integer.toString(val);
							_lStack.get(_lStack.size()-1).push(sVal);
							return;
						} else {
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return;
						}
					} else if (isInt(sVal1)) {
						int val1 = Integer.parseInt(sVal1);
						if (val1 == 0) {
							return;
						}
						int val = -1 * val1;
						String sVal = Integer.toString(val);
						_lStack.get(_lStack.size()-1).push(sVal);
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} catch (Exception ex) {
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("swap")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				_lStack.get(_lStack.size()-1).push(sVal1);
				_lStack.get(_lStack.size()-1).push(sVal2);
			}
		} else if (command.contains("cat")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if(isName(sVal1) && isName(sVal2)) {
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isString(_lBind.get(_lBind.size()-1).get(sVal1))
							&& isString(_lBind.get(_lBind.size()-1).get(sVal2))) {
						sVal1 = _lBind.get(_lBind.size()-1).get(sVal1);
						sVal2 = _lBind.get(_lBind.size()-1).get(sVal2);
						String catVal = sVal2.substring(0, sVal2.length() - 1) + sVal1.substring(1);
						_lStack.get(_lStack.size()-1).push(catVal);
						return; 
					}
					else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if(isName(sVal1) && !isName(sVal2)) {
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isString(_lBind.get(_lBind.size()-1).get(sVal1)) && isString(sVal2)) {
						sVal1 = _lBind.get(_lBind.size()-1).get(sVal1);
						String catVal = sVal2.substring(0, sVal2.length() - 1) + sVal1.substring(1);
						_lStack.get(_lStack.size()-1).push(catVal);						
						return; 
					}else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if(!isName(sVal1) && isName(sVal2)) {
					if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isString(_lBind.get(_lBind.size()-1).get(sVal2)) && isString(sVal1)) {
						sVal2 = _lBind.get(_lBind.size()-1).get(sVal2);
						String catVal = sVal2.substring(0, sVal2.length() - 1) + sVal1.substring(1);
						_lStack.get(_lStack.size()-1).push(catVal);
						return; 
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isString(sVal1) && isString(sVal2)) {
					String catVal = sVal2.substring(0, sVal2.length() - 1) + sVal1.substring(1);
					_lStack.get(_lStack.size()-1).push(catVal);
				} else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return; 
				}
			}
		} else if (command.contains("and")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if (isName(sVal1) && isName(sVal2)) { // both values are names
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isBool(_lBind.get(_lBind.size()-1).get(sVal1))
							&& isBool(_lBind.get(_lBind.size()-1).get(sVal2))) {
						String v1 = _lBind.get(_lBind.size()-1).get(sVal1);
						String v2 = _lBind.get(_lBind.size()-1).get(sVal2);
						if (v1.equals(":true:") && v2.equals(":true:")) {
							_lStack.get(_lStack.size()-1).push(":true:");
						} else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isBool(_lBind.get(_lBind.size()-1).get(sVal1)) && isBool(sVal2)) {
						String v1 = _lBind.get(_lBind.size()-1).get(sVal1);
						String v2 = sVal2;
						if (v1.equals(":true:") && v2.equals(":true:")) {
							_lStack.get(_lStack.size()-1).push(":true:");
						} else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isBool(_lBind.get(_lBind.size()-1).get(sVal2)) && isBool(sVal1)) {
						String v1 = sVal1;
						String v2 = _lBind.get(_lBind.size()-1).get(sVal2);
						if (v1.equals(":true:") && v2.equals(":true:")) {
							_lStack.get(_lStack.size()-1).push(":true:");
						} else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isBool(sVal1) && isBool(sVal2)) {
					if (sVal1.equals(":true:") && sVal2.equals(":true:")) {
						_lStack.get(_lStack.size()-1).push(":true:");
					} else {
						_lStack.get(_lStack.size()-1).push(":false:");
					}
					return;
				} else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("or")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if (isName(sVal1) && isName(sVal2)) { // both values are names
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isBool(_lBind.get(_lBind.size()-1).get(sVal1))
							&& isBool(_lBind.get(_lBind.size()-1).get(sVal2))) {
						String v1 = _lBind.get(_lBind.size()-1).get(sVal1);
						String v2 = _lBind.get(_lBind.size()-1).get(sVal2);
						if (v1.equals(":false:") && v2.equals(":false:")) {
							_lStack.get(_lStack.size()-1).push(":false:");
						} else {
							_lStack.get(_lStack.size()-1).push(":true:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isBool(_lBind.get(_lBind.size()-1).get(sVal1)) && isBool(sVal2)) {
						String v1 = _lBind.get(_lBind.size()-1).get(sVal1);
						String v2 = sVal2;
						if (v1.equals(":false:") && v2.equals(":false:")) {
							_lStack.get(_lStack.size()-1).push(":false:");
						} else {
							_lStack.get(_lStack.size()-1).push(":true:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isBool(_lBind.get(_lBind.size()-1).get(sVal2)) && isBool(sVal1)) {
						String v1 = sVal1;
						String v2 = _lBind.get(_lBind.size()-1).get(sVal2);
						if (v1.equals(":false:") && v2.equals(":false:")) {
							_lStack.get(_lStack.size()-1).push(":false:");
						} else {
							_lStack.get(_lStack.size()-1).push(":true:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isBool(sVal1) && isBool(sVal2)) {
					if (sVal1.equals(":false:") && sVal2.equals(":false:")) {
						_lStack.get(_lStack.size()-1).push(":false:");
					} else {
						_lStack.get(_lStack.size()-1).push(":true:");
					}
					return;
				} else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return; 
				}
			}
		} else if (command.contains("not")) {
			if (_lStack.get(_lStack.size()-1).isEmpty()) {
				System.out.println("MESSED");
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				if(isName(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal1) && isBool(_lBind.get(_lBind.size()-1).get(sVal1))) {
					String v1 = _lBind.get(_lBind.size()-1).get(sVal1); 
					if (v1.equals(":true:")) {
						_lStack.get(_lStack.size()-1).push(":false:");
					} else {
						_lStack.get(_lStack.size()-1).push(":true:");
					}
					return; 
				}
				else if (isBool(sVal1)) {
					if (sVal1.equals(":true:")) {
						_lStack.get(_lStack.size()-1).push(":false:");
					} else {
						_lStack.get(_lStack.size()-1).push(":true:");
					}
					return; 
				} else {
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return; 
				}
			}
		} else if (command.contains("equal")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if (isName(sVal1) && isName(sVal2)) { // both values are names
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
							&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
						int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
						int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
						if (val1 == (val2)) {
							_lStack.get(_lStack.size()-1).push(":true:");
						}else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
						int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
						int val2 = Integer.parseInt(sVal2);
						if (val1 == (val2)) {
							_lStack.get(_lStack.size()-1).push(":true:");
						}else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
						if (val1 == (val2)) {
							_lStack.get(_lStack.size()-1).push(":true:");
						}else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				}
				if (isInt(sVal1) && isInt(sVal2)) {
					if (sVal1.equals(sVal2)) {
						_lStack.get(_lStack.size()-1).push(":true:");
					}else {
						_lStack.get(_lStack.size()-1).push(":false:");
					}
					return; 
				} else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return; 
				}
			}
		} else if (command.contains("lessThan")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if (isName(sVal1) && isName(sVal2)) { // both values are names
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && _lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(_lBind.get(_lBind.size()-1).get(sVal1))
							&& isInt(_lBind.get(_lBind.size()-1).get(sVal2))) {
						int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
						int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
						if (val1 > val2) {
							_lStack.get(_lStack.size()-1).push(":true:");
						} else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (isName(sVal1) && !isName(sVal2)) { // val1 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal1) && isInt(sVal2)) {
						int val1 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal1));
						int val2 = Integer.parseInt(sVal2);
						if (val1 > val2) {
							_lStack.get(_lStack.size()-1).push(":true:");
						} else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				} else if (!isName(sVal1) && isName(sVal2)) { // val2 is a name
					if (_lBind.get(_lBind.size()-1).containsKey(sVal2) && isInt(sVal1)) {
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(_lBind.get(_lBind.size()-1).get(sVal2));
						if (val1 > val2) {
							_lStack.get(_lStack.size()-1).push(":true:");
						} else {
							_lStack.get(_lStack.size()-1).push(":false:");
						}
						return;
					} else {
						_lStack.get(_lStack.size()-1).push(sVal2);
						_lStack.get(_lStack.size()-1).push(sVal1);
						_lStack.get(_lStack.size()-1).push(":error:");
						return;
					}
				}
				if (isInt(sVal1) && isInt(sVal2)) {
					Integer v1 = Integer.parseInt(sVal1);
					Integer v2 = Integer.parseInt(sVal2);
					if (v1 > v2) {
						_lStack.get(_lStack.size()-1).push(":true:");
					} else {
						_lStack.get(_lStack.size()-1).push(":false:");
					}
				} else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return; 
				}
			}
		} else if (command.contains("bind")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if (isName(sVal2) && !sVal1.equals(":error:")) {
					if (isName(sVal1)) {
						if (_lBind.get(_lBind.size()-1).containsKey(sVal1)) {
							String v = _lBind.get(_lBind.size()-1).get(sVal1);
							_lBind.get(_lBind.size()-1).put(sVal2, v);
							_lStack.get(_lStack.size()-1).push(":unit:");
							return; 
						} else {
							_lStack.get(_lStack.size()-1).push(sVal2);
							_lStack.get(_lStack.size()-1).push(sVal1);
							_lStack.get(_lStack.size()-1).push(":error:");
							return; 
						}
					} else {
						_lBind.get(_lBind.size()-1).put(sVal2, sVal1);
						_lStack.get(_lStack.size()-1).push(":unit:");
						return; 
					}
				} else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return; 
				}
			}
		} else if (command.contains("if")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() <= 2) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				String sVal3 = _lStack.get(_lStack.size()-1).pop();
				if(isName(sVal3) && isBool(_lBind.get(_lBind.size()-1).get(sVal3))) { // val3 is a name and a boolean
					if(_lBind.get(_lBind.size()-1).get(sVal3).equals(":true:")) {
						_lStack.get(_lStack.size()-1).push(sVal2);
					} else {
						_lStack.get(_lStack.size()-1).push(sVal1);
					}
				} else if(isBool(sVal3)) { // val3 is a bool 
					if(sVal3.equals(":true:")) {
						_lStack.get(_lStack.size()-1).push(sVal2);
					} else {
						_lStack.get(_lStack.size()-1).push(sVal1);
					}
				} else {
					_lStack.get(_lStack.size()-1).push(sVal3);
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		} else if (command.contains("let")) {
			_lStack.add(new Stack<String>()); 
			_lBind.add((HashMap<String, String>) _lBind.get(_lBind.size()-1).clone());
			_lStack.get(_lStack.size()-2).push("let");
			inLet = true; 
			return; 
		} else if (command.contains("end")) {
			if(_lStack.get(_lStack.size()-2).peek().equals(("let"))) {
				if(inLet && !_store.isEmpty()) {
					System.out.println("Store :" + _store);
					_lFun.remove(_lFun.get(_lFun.size()-1).remove(_store.pop()));
					inLet = false;
				}
				_lStack.get(_lStack.size()-2).pop();
				String valP = _lStack.get(_lStack.size()-1).pop(); 
				_lStack.get(_lStack.size()-2).push(valP);
				_lStack.remove(_lStack.get(_lStack.size()-1));
				_lBind.remove(_lBind.get(_lBind.size()-1));
			}
			return;
		} else if (command.contains("call")) {
			if (_lStack.get(_lStack.size()-1).isEmpty() || _lStack.get(_lStack.size()-1).size() == 1) {
				_lStack.get(_lStack.size()-1).push(":error:");
				return;
			} else {
				String sVal1 = _lStack.get(_lStack.size()-1).pop();
				String sVal2 = _lStack.get(_lStack.size()-1).pop();
				if(isFun(sVal2) && !sVal1.equals(":error:")) {
					if(isName(sVal1)) {
						sVal1 = ("push " + _lBind.get(_lBind.size()-1).get(sVal1));
					}else {
						sVal1 = ("push " + sVal1);
					}
					int x = _lStack.size()-1;
					ArrayList<String> calls = _lFun.get(_lFun.size()-1).get(sVal2); //make a list to store method body
					System.out.println("Calls" + calls);
					Stack<String> toP = new Stack<String>();
					_lStack.add(toP);
					System.out.println(_bind);
					_lBind.add((HashMap<String, String>) _bind.get(sVal2));
					for(int i=1; i<calls.size();++i) {
						String arg = "";
						if(calls.get(i).contains("return")){
							String valP = _lStack.get(_lStack.size()-1).pop();  
							_lStack.get(_lStack.size()-2).push(valP);
							_lStack.remove(_lStack.get(_lStack.size()-1));
							_lBind.remove(_lBind.get(_lBind.size()-1));
							return; 
						}
						if(calls.get(i).contains("push")) {
							String[] v = calls.get(i).split(" ");
							if(v[1].equals(calls.get(0))) {
								interpret(sVal1);
								continue;
							} else {
								interpret(calls.get(i));
							}
						} else {
							interpret(calls.get(i));
						}
					}
					return;
				}else {
					_lStack.get(_lStack.size()-1).push(sVal2);
					_lStack.get(_lStack.size()-1).push(sVal1);
					_lStack.get(_lStack.size()-1).push(":error:");
					return;
				}
			}
		}
		
	}
	
	private static boolean isFun(String s) {
		try {
			return (_lFun.get(_lFun.size()-1).containsKey(s));
		}
		catch(Exception x) {
			return false;
		}
	}

	private static boolean isName(String s) {
		return (!(isBool(s) || isString(s) || isInt(s) || s.equals(":error:") || s.equals(":unit:")));
	}

	private static boolean isBool(String s) {
		return (s.equals(":true:") || s.equals(":false:"));
	}

	private static boolean isString(String s) {
		return (s.contains("\""));
	}

	private static boolean isInt(String s) throws IndexOutOfBoundsException, NumberFormatException {
		try {
			Double temp = Double.parseDouble(s);
			if (temp - (int) (temp / 1) == 0.0) {
				return true;
			} else {
				throw new IndexOutOfBoundsException();
			}
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static void main(String[] args) {
		interpreter("src/input.txt", "src/output.txt");
	}
}
