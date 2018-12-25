package sc.ustc.di;

import java.util.LinkedList;

import sc.ustc.tools.XMLTool;

public class DiBean {
	
//	private static String diPath = Thread.currentThread().getContextClassLoader().
//			getResource("di.xml").getPath();
	private static String diPath = "D:\\JavaProject\\SimpleMVC\\UseSC\\src\\di.xml";
	
	private String id;
	private String fieldName;
	private String clasS;
	private LinkedList<DiField> beanFields;
	//用树形结构存储依赖关系
	private DiBean parentBean;
	private LinkedList<DiBean> childBeans;
	private boolean isLeafNode;
	
	public DiBean() {
		// TODO Auto-generated constructor stub
	}
	
	public DiBean(String id, String clasS) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.clasS = clasS;
		this.beanFields = new LinkedList<>();
		
		this.parentBean = null;
		this.isLeafNode = false;
		this.childBeans = new LinkedList<>();
	}

	public String getID() {
		return id;
	}

	public String getClasS() {
		return clasS;
	}

	public LinkedList<DiField> getDiFields() {
		return beanFields;
	}

	public void addDiField(DiField beanField) {
		this.beanFields.add(beanField);
	}

	public DiBean getParentBean() {
		return parentBean;
	}

	public LinkedList<DiBean> getChildBeans() {
		return childBeans;
	}

	public void setParentBean(DiBean parentBean) {
		this.parentBean = parentBean;
	}

	public void addChildBeans(DiBean childBean) {
		this.childBeans.add(childBean);
	}

	public boolean isLeafNode() {
		return isLeafNode;
	}

	public void setLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public LinkedList<DiBean> createDiTree(String beanClass) {
		LinkedList<DiBean> tree = new LinkedList<>();
		//解析根节点，即action对应的bean
		XMLTool tool = new XMLTool(diPath);
		DiBean root = tool.readDi(null, beanClass);
		root.setParentBean(null);
		root.setFieldName(null);
		tree.add(root);
		//添加子结点
		LinkedList<DiBean> nodes = new LinkedList<>();
		nodes.add(root);
		while(!nodes.isEmpty()) {
			DiField field = null;
			DiBean node = nodes.pollFirst();
			int count = 0;
			while(!node.getDiFields().isEmpty()) {
				field = node.getDiFields().pollFirst();
				DiBean childNode = tool.readDi(field.getBeanRef(), null);
				childNode.setParentBean(node);
				node.addChildBeans(childNode);
				childNode.setFieldName(field.getName());
				nodes.add(childNode);
				tree.add(childNode);
				count++;
			}
			if(count == 0) {
				node.setLeafNode(true);
			}
		}
		return tree;
	}
	
	public String display(LinkedList<DiBean> tree) {
		String s = "\r\n";
		int i = 0;
		for(DiBean db: tree) {
			s += (i++) + "_BeanID:" + db.getID() + "\r\n"
					+ "BeanClass:" + db.getClasS() + "\r\n"
					+ "parent:" + (db.getParentBean() == null? 
							"null":db.getParentBean().getID()) + "\r\n"
					+ "child:" + db.getChildBeans().size() + "\r\n"
					+ "isLeaf:" + db.isLeafNode + "\r\n";
		}
		return s;
	}
	
	public static void main(String[] args) {
		DiBean bean = new DiBean();
		System.out.println(bean.display(bean.createDiTree("water.ustc.action.LoginAction")));
		
	}

}
