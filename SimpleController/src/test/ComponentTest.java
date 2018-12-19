package test;

import sc.ustc.view.components.ButtonView;
import sc.ustc.view.components.CheckBox;
import sc.ustc.view.components.ListOption;
import sc.ustc.view.components.ListView;
import sc.ustc.view.components.RadioBox;
import sc.ustc.view.components.RadioGroup;
import sc.ustc.view.components.TextField;
import sc.ustc.view.components.TextView;

public class ComponentTest {
	
	public static void main(String[] args) {
		TextView textView = new TextView("textView");
//		textView.setLabel("This is a TextView:", "");
		textView.setClick(true);
		textView.setMotion("/UseSC/views/welcome.jsp");
		textView.setaLabel("Jump");
		System.out.println(textView.toJsp());
		
		ButtonView buttonView = new ButtonView("buttonView");
//		buttonView.setLabel("", "This is a ButtonView.");
		buttonView.setValue("button");
		System.out.println(buttonView.toJsp());
		
		TextField textField = new TextField("textField");
//		textField.setLabel("ID:", "");
		System.out.println(textField.toJsp());
		
		TextField pswField = new TextField("pswField");
//		pswField.setLabel("PSW:", "");
		pswField.setPassword(true);
		System.out.println(pswField.toJsp());
		
		CheckBox checkBox = new CheckBox("checkBox");
//		checkBox.setLabel("Check:", "This is a CheckBox.");
//		checkBox.setChecked(true);
		System.out.println(checkBox.toJsp());
		
		RadioGroup radioGroup = new RadioGroup("radioGroup");
		RadioBox box0 = new RadioBox("1");
		box0.setLabel("", "male");
		RadioBox box1 = new RadioBox("0");
		box1.setLabel("", "female");
		box1.setChecked(true);
		radioGroup.addRadioBox(box0);
		radioGroup.addRadioBox(box1);
		System.out.println(radioGroup.toJsp());
		
		ListView listView = new ListView("listView");
//		listView.setLabel("Choose", "");
		ListOption o0 = new ListOption("0");
		o0.setItem("--choose--");
		o0.setSelected(true);
		ListOption o1 = new ListOption("1");
		o1.setItem("1996");
		ListOption o2 = new ListOption("2");
		o2.setItem("1997");
		ListOption o3 = new ListOption("3");
		o3.setItem("1998");
		ListOption o4 = new ListOption("4");
		o4.setItem("1999");
		listView.addOption(o0);
		listView.addOption(o1);
		listView.addOption(o2);
		listView.addOption(o3);
		listView.addOption(o4);
		System.out.println(listView.toJsp());
		
	}

}
