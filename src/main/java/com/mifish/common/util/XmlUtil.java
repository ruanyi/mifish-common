package com.mifish.common.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XmlUtil
 *
 * @author ruanlsh
 * @date:2014-01-06
 */
public final class XmlUtil {

    private XmlUtil() {
    }

    /**
     * getChildElement
     * <p>
     * if it does not exist,return null
     * find the first match
     *
     * @param parent
     * @param childName
     * @return Element
     */
    public static Element getChildElement(Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        int size = children.getLength();
        for (int i = 0; i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (childName.equals(element.getNodeName())) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * getChildElements
     *
     * @param parent
     * @param childName
     * @return List<Element>
     */
    public static List<Element> getChildElements(Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        List<Element> list = new ArrayList<Element>();
        for (int i = 0, size = children.getLength(); i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (childName.equals(element.getNodeName())) {
                    list.add(element);
                }
            }
        }
        return list;
    }

    /**
     * getChildText
     *
     * @param parent
     * @param childName
     * @return String
     */
    public static String getChildText(Element parent, String childName) {
        Element child = getChildElement(parent, childName);
        if (child == null) {
            return null;
        }
        return getText(child);
    }

    /**
     * getText
     *
     * @param node
     * @return String
     */
    public static String getText(Element node) {
        StringBuilder sb = new StringBuilder();
        NodeList list = node.getChildNodes();
        //
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            switch (child.getNodeType()) {
                case Node.CDATA_SECTION_NODE:
                case Node.TEXT_NODE:
                    sb.append(child.getNodeValue());
            }
        }
        return sb.toString();
    }
}
