package sc.ustc.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sc.ustc.view.components.ButtonView;
import sc.ustc.view.components.CheckBox;
import sc.ustc.view.components.ListOption;
import sc.ustc.view.components.ListView;
import sc.ustc.view.components.RadioBox;
import sc.ustc.view.components.RadioGroup;
import sc.ustc.view.components.TextField;
import sc.ustc.view.components.TextView;

public class XMLbyDOM {
	
	public String readView(String path) {
		StringBuilder jsp = new StringBuilder();
		jsp.append("<html>\r\n");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbuilder = dbf.newDocumentBuilder();
			Document docu = dbuilder.parse(new File(path));
			//解析header节点
			NodeList header = docu.getElementsByTagName("header");
			if(header.getLength() > 0) {
				Element headerItem = (Element)header.item(0);
				String title = headerItem.getTextContent().trim();
				jsp.append("<head>\r\n");
				jsp.append("<meta http-equiv=\"Content-Type\" content=\"text/html;"
						+ " charset=ISO-8859-1\">\r\n");
				jsp.append("<title>" + title + "</title>\r\n");
				jsp.append("</head>\r\n");
			}
			jsp.append("<body>\r\n");
			
			//解析form节点
			NodeList forms = docu.getElementsByTagName("form");
			for(int i = 0; i < forms.getLength(); i++) {
				jsp.append("<form \r\n");
				Node fNode = forms.item(i).getFirstChild();
				int count = 0;
				while(fNode != null) {
					String nodeName = fNode.getNodeName();
					if(nodeName.equals("#text")) {
						fNode = fNode.getNextSibling();
					}
					else if(count < 3) {
						//解析form的name、action、method属性
						if(nodeName.equals("name")) {
							String name = fNode.getTextContent().trim();
							jsp.append("name=\"" + name + "\"");
							count++;
						}
						else if(nodeName.equals("action")) {
							String action = fNode.getTextContent().trim();
							jsp.append("action=\"" + action + "\"");
							count++;
						}
						else if(nodeName.equals("method")) {
							String method = fNode.getTextContent().trim();
							jsp.append("method=\"" + method + "\"");
							count++;
						}
						fNode = fNode.getNextSibling();
						if(count == 3) {
							jsp.append(">\r\n");
						}
						else if(count < 3){
							jsp.append("\r\n");
						}
					}
					else {
						//解析视图中的控件
						String s;
						switch (fNode.getNodeName()) {
						case "textView":
							if(!(s = parseTextView(fNode)).equals("")) {
								jsp.append(s);
							}
							break;
						case "textField":
							if(!(s = parseTextField(fNode)).equals("")) {
								jsp.append(s);
							}
							break;
						case "buttonView":
							if(!(s = parseButtonView(fNode)).equals("")) {
								jsp.append(s);
							}
							break;
						case "checkBox":
							if(!(s = parseCheckBox(fNode)).equals("")) {
								jsp.append(s);
							}
							break;
						case "radioGroup":
							if(!(s = parseRadioGroup(fNode)).equals("")) {
								jsp.append(s);
							}
							break;
						case "listView":
							if(!(s = parseListView(fNode)).equals("")) {
								jsp.append(s);
							}
							break;
						default:
							break;
						}
						fNode = fNode.getNextSibling();
					}
				}

				jsp.append("</form>\r\n");
			}
			jsp.append("</body>\r\n");
			jsp.append("</html>\r\n");
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsp.toString();
	}
	
	private String parseTextView(Node fNode) {
		// TODO Auto-generated method stub
		NodeList nodes = fNode.getChildNodes();
		TextView view = null;
		//用该控件的名字new一个该控件的对象
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("name")) {
				view = new TextView(nodes.item(i).getTextContent().trim());
				break;
			}
		}
		if(view != null) {
			//如果new成功，则解析其属性
			for(int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeName().equals("label-front")) {
					view.setLabelFront(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("label-behind")) {
					view.setLabelBehind(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("value")) {
					view.setValue(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("isClick")) {
					if(node.getTextContent().trim().equals("true")) {
						view.setClick(true);
					}
					else {
						view.setClick(false);
					}
				}
				else if(node.getNodeName().equals("href")) {
					view.setMotion(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("href-label")) {
					view.setaLabel(node.getTextContent().trim());
				}
			}
			return view.toJsp();
		}
		return "";
	}

	private String parseTextField(Node fNode) {
		// TODO Auto-generated method stub
		NodeList nodes = fNode.getChildNodes();
		TextField view = null;
		//用该控件的名字new一个该控件的对象
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("name")) {
				view = new TextField(nodes.item(i).getTextContent().trim());
				break;
			}
		}
		if(view != null) {
			//如果new成功，则解析其属性
			for(int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeName().equals("label-front")) {
					view.setLabelFront(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("label-behind")) {
					view.setLabelBehind(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("isPassword")) {
					if(node.getTextContent().trim().equals("true")) {
						view.setPassword(true);
					}
					else {
						view.setPassword(false);
					}
				}
			}
			return view.toJsp();
		}
		return "";
	}

	private String parseButtonView(Node fNode) {
		// TODO Auto-generated method stub
		NodeList nodes = fNode.getChildNodes();
		ButtonView view = null;
		//用该控件的名字new一个该控件的对象
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("name")) {
				view = new ButtonView(nodes.item(i).getTextContent().trim());
				break;
			}
		}
		if(view != null) {
			//如果new成功，则解析其属性
			for(int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeName().equals("label-front")) {
					view.setLabelFront(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("label-behind")) {
					view.setLabelBehind(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("value")) {
					view.setValue(node.getTextContent().trim());
				}
			}
			return view.toJsp();
		}
		return "";
	}

	private String parseCheckBox(Node fNode) {
		// TODO Auto-generated method stub
		NodeList nodes = fNode.getChildNodes();
		CheckBox view = null;
		//用该控件的名字new一个该控件的对象
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("name")) {
				view = new CheckBox(nodes.item(i).getTextContent().trim());
				break;
			}
		}
		if(view != null) {
			//如果new成功，则解析其属性
			for(int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeName().equals("label-front")) {
					view.setLabelFront(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("label-behind")) {
					view.setLabelBehind(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("isChecked")) {
					if(node.getTextContent().trim().equals("true")) {
						view.setChecked(true);
					}
					else {
						view.setChecked(false);
					}
				}
			}
			return view.toJsp();
		}
		return "";
	}

	private String parseRadioGroup(Node fNode) {
		// TODO Auto-generated method stub
		NodeList nodes = fNode.getChildNodes();
		RadioGroup view = null;
		//用该控件的名字new一个该控件的对象
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("name")) {
				view = new RadioGroup(nodes.item(i).getTextContent().trim());
				break;
			}
		}
		if(view != null) {
			//如果new成功，则解析其属性
			for(int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeName().equals("label-front")) {
					view.setLabelFront(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("label-behind")) {
					view.setLabelBehind(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("radioBox")) {
					//解析每个单选框
					NodeList radios = node.getChildNodes();
					String value = "";
					String labelF = "";
					String labelB = "";
					boolean isChecked = false;
					for(int j = 0; j < radios.getLength(); j++) {
						Node radioNode = radios.item(j);
						if(radioNode.getNodeName().equals("value")) {
							value = radioNode.getTextContent().trim();
						}
						else if(radioNode.getNodeName().equals("label-front")) {
							labelF = radioNode.getTextContent().trim();
						}
						else if(radioNode.getNodeName().equals("label-behind")) {
							labelB = radioNode.getTextContent().trim();
						}
						else if(radioNode.getNodeName().equals("isChecked")) {
							if(radioNode.getTextContent().trim().equals("true")) {
								isChecked = true;
							}
							else {
								isChecked = false;
							}
						}
					}
					if(!value.equals("")) {
						RadioBox radioBox = new RadioBox(value);
						radioBox.setLabel(labelF, labelB);
						radioBox.setChecked(isChecked);
						view.addRadioBox(radioBox);
					}
				}
			}
			return view.toJsp();
		}
		return "";
	}

	private String parseListView(Node fNode) {
		// TODO Auto-generated method stub
		NodeList nodes = fNode.getChildNodes();
		ListView view = null;
		//用该控件的名字new一个该控件的对象
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("name")) {
				view = new ListView(nodes.item(i).getTextContent().trim());
				break;
			}
		}
		if(view != null) {
			//如果new成功，则解析其属性
			for(int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeName().equals("label-front")) {
					view.setLabelFront(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("label-behind")) {
					view.setLabelBehind(node.getTextContent().trim());
				}
				else if(node.getNodeName().equals("option")) {
					//解析每个单选框
					NodeList radios = node.getChildNodes();
					String value = "";
					String item = "";
					boolean isSelected = false;
					for(int j = 0; j < radios.getLength(); j++) {
						Node radioNode = radios.item(j);
						if(radioNode.getNodeName().equals("value")) {
							value = radioNode.getTextContent().trim();
						}
						else if(radioNode.getNodeName().equals("item")) {
							item = radioNode.getTextContent().trim();
						}
						else if(radioNode.getNodeName().equals("isChecked")) {
							if(radioNode.getTextContent().trim().equals("true")) {
								isSelected = true;
							}
							else {
								isSelected = false;
							}
						}
					}
					if(!value.equals("")) {
						ListOption option = new ListOption(value);
						option.setItem(item);
						option.setSelected(isSelected);
						view.addOption(option);
					}
				}
			}
			return view.toJsp();
		}
		return "";
	}

}
