package com.kuc_arc_f.app.picasa;
//Item.java
public class ItemSearch {
	private CharSequence m_Key;
	private CharSequence m_UID;
	
	public ItemSearch() {
		m_Key = "";
	}
	public CharSequence getKey() {
		return m_Key;
	}
	public void setKey(CharSequence key) {
		m_Key = key;
	}
	public CharSequence getUID(){ return m_UID;}
	public void setUID(String src){m_UID=src;}

}