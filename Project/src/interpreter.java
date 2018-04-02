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

public class interpreter{

	private static Stack<String> _store = new Stack<String>();
	private static HashMap<String,String> _bind = new HashMap<String,String>();

	public static void interpreter(String input, String output){
		ArrayList<String> inputList = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String temp;
			while((temp = reader.readLine()) != null){
				inputList.add(temp);
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			for(int i=0; i<inputList.size(); ++i){
				interpret(inputList.get(i));
			}
			while(!_store.isEmpty()){
//				if(_store.peek()=="quit"){
//					return;
//				}
				String toPush = _store.pop() + "\n"; 
				if(toPush.contains("\"")) {
					toPush = toPush.substring(1,toPush.length()-1);
				}
				writer.write(toPush);
			}
			writer.close();
			System.out.println("It worked");
			return;

		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private static void interpret(String command){
		if(command.contains("quit")){
			return;
		}
//		if(!_store.isEmpty() && _store.peek()==":error:"){
//			_store.push(":error:");
//			return;
//		}
		if(command.contains("push")){
			String numPush = command.substring(5,command.length());
			try{
				if(numPush.contains("-0")){
					_store.push("0");
					return;
				}else if(numPush.charAt(0)== '-' && !isInt(numPush.substring(1))){
					System.out.println("should work");
					_store.push(":error:");
					return;
				}
				else if(isInt(numPush)){
						_store.push(numPush);
						return;
//				}else if (numPush.contains("\"")){
//					_store.push(numPush.substring(1, numPush.length()-1));
//					return;
				}else{
					_store.push(numPush);
					return;
				}
			}
			catch(IndexOutOfBoundsException ex){
				_store.push(":error:");
				return;
			}
		}else if(command.contains("pop")){
			try{
				_store.pop();
				return;
			}
			catch(EmptyStackException ex){
				_store.push(":error:");
				return;
			}
		}
		else if(command.contains("add")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val1 + val2;
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("mul")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val1 * val2;
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("sub")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						int val2 = Integer.parseInt(sVal2);
						int val = val2 - val1;
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("div")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						if(val1 == 0){
							_store.push(sVal2);
							_store.push(sVal1);
							_store.push(":error:");
							return;
						}
						int val2 = Integer.parseInt(sVal2);
						int val = val2 / val1;
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("rem")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				try{
					if(isInt(sVal1) && isInt(sVal2)){
						int val1 = Integer.parseInt(sVal1);
						if(val1 == 0){
							_store.push(sVal2);
							_store.push(sVal1);
							_store.push(":error:");
							return;
						}
						int val2 = Integer.parseInt(sVal2);
						int val = val2 % val1;
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal2);
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("neg")){
			if(_store.isEmpty()){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				try{
					if(isInt(sVal1)){
						int val1 = Integer.parseInt(sVal1);
						if(val1 == 0){
							return;
						}
						int val = -1 * val1;
						String sVal = Integer.toString(val);
						_store.push(sVal);
						return;
					}else{
						_store.push(sVal1);
						_store.push(":error:");
						return;
					}
				}
				catch(IndexOutOfBoundsException ex){
					_store.push(sVal1);
					_store.push(":error:");
					return;
				}
			}
		}else if(command.contains("swap")){
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				_store.push(sVal1);
				_store.push(sVal2);
			}
		}else if(command.contains("cat")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else{
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				if(isString(sVal1)&&isString(sVal2)){
					String catVal = sVal2.substring(0, sVal2.length()-1)+ sVal1.substring(1);
					_store.push(catVal);
				}else {
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}else if(command.contains("and")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else {
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				if(isBool(sVal1)&&isBool(sVal2)){
					if(sVal1.equals(":true:") && sVal2.equals(":true:")) {
						_store.push(":true:");
					}else {
						_store.push(":false:");
					}
				}else {
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}else if(command.contains("or")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else {
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				if(isBool(sVal1)&&isBool(sVal2)){
					if(sVal1.equals(":false:") && sVal2.equals(":false:")) {
						_store.push(":false:");
					}else {
						_store.push(":true:");
					}
				}else {
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}else if(command.contains("not")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else {
				String sVal1 = _store.pop();
				if(isBool(sVal1)){
					if(sVal1.equals(":true:")) {
						_store.push(":false:");
					}else {
						_store.push(":true:");
					}
				}else {
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}else if(command.contains("equal")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else {
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				if(isInt(sVal1)&&isInt(sVal2)&&(sVal1.equals(sVal2))){
					_store.push(":true:");
				}else {
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}else if(command.contains("lessThan")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else {
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				if(isInt(sVal1)&&isInt(sVal2)){
					Integer v1 = Integer.parseInt(sVal1);
					Integer v2 = Integer.parseInt(sVal2);
					if(v1>v2) {
						_store.push(":true:");
					}else {
						_store.push(":false:");
					}
				}else {
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}else if(command.contains("bind")) {
			if(_store.isEmpty() || _store.size()==1){
				_store.push(":error:");
				return;
			}else {
				String sVal1 = _store.pop();
				String sVal2 = _store.pop();
				if(isName(sVal2)&&!sVal1.equals(":error:")){
					if(isName(sVal1)){
						if(_bind.containsKey(sVal1)) {
							String v = _bind.get(sVal1);
							_bind.put(sVal2, v);
							_store.push(":unit:");
						}else {
							_store.push(sVal2);
							_store.push(sVal1);
							_store.push(":error:");
						}
					}else {
						_bind.put(sVal2, sVal1);
						_store.push(":unit:");
					}
				}else {
					_store.push(sVal2);
					_store.push(sVal1);
					_store.push(":error:");
				}
			}
		}
	}
	private static boolean isName(String s) {
		return (!(isBool(s)||isString(s)||isInt(s)||s.equals(":error:")||s.equals(":unit:")));
	}
	private static boolean isBool(String s) {
		return (s.equals(":true:") || s.equals(":false:"));
	}
	private static boolean isString(String s) {
		return (s.contains("\""));
	}
	private static boolean isInt(String s)throws IndexOutOfBoundsException, NumberFormatException{
	    try{
	        Double temp = Double.parseDouble(s);
	        if(temp - (int)(temp/1) == 0.0){
	        	return true;
	        }
	        else{
	        	throw new IndexOutOfBoundsException();
	        }
	    } catch (NumberFormatException ex){
	    	return false;
	    }
	}

	public static void main(String[] args){
		interpreter("input.txt", "output.txt");
	}
}
