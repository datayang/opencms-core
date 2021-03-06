/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH & Co. KG (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH & Co. KG, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.setup.xml.v8;

import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.configuration.CmsVfsConfiguration;
import org.opencms.configuration.I_CmsXmlConfiguration;
import org.opencms.loader.CmsMacroFormatterLoader;
import org.opencms.loader.CmsRedirectLoader;
import org.opencms.loader.CmsSeoFileLoader;
import org.opencms.loader.CmsXmlContainerPageLoader;
import org.opencms.setup.CmsSetupBean;
import org.opencms.setup.xml.A_CmsXmlVfs;
import org.opencms.setup.xml.CmsSetupXmlHelper;
import org.opencms.util.CmsStringUtil;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * Adds the new loader classes.<p>
 *
 * @since 8.0.0
 */
public class CmsXmlAddLoaders extends A_CmsXmlVfs {

    /** The loader classes to add. */
    private static final Class<?>[] LOADER_CLASSES = new Class[] {
        CmsXmlContainerPageLoader.class,
        CmsRedirectLoader.class,
        CmsSeoFileLoader.class,
        CmsMacroFormatterLoader.class};

    /** List of xpaths to update. */
    private List<String> m_xpaths;

    /**
     * @see org.opencms.setup.xml.I_CmsSetupXmlUpdate#getName()
     */
    public String getName() {

        return "Add new loader classes";
    }

    /**
     * @see org.opencms.setup.xml.I_CmsSetupXmlUpdate#validate(org.opencms.setup.CmsSetupBean)
     */
    @Override
    public boolean validate(CmsSetupBean setupBean) throws Exception {

        return CmsStringUtil.isNotEmptyOrWhitespaceOnly(getCodeToChange(setupBean));
    }

    /**
     * @see org.opencms.setup.xml.A_CmsSetupXmlUpdate#executeUpdate(org.dom4j.Document, java.lang.String, boolean)
     */
    @Override
    protected boolean executeUpdate(Document document, String xpath, boolean forReal) {

        Node node = document.selectSingleNode(xpath);
        if (node == null) {
            for (int i = 0; i < LOADER_CLASSES.length; i++) {

                if (xpath.contains(LOADER_CLASSES[i].getName())) {
                    CmsSetupXmlHelper.setValue(
                        document,
                        xpath + "/@" + I_CmsXmlConfiguration.A_CLASS,
                        LOADER_CLASSES[i].getName());
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * @see org.opencms.setup.xml.A_CmsSetupXmlUpdate#getCommonPath()
     */
    @Override
    protected String getCommonPath() {

        // /opencms/vfs/resources/resourceloaders
        return new StringBuffer("/").append(CmsConfigurationManager.N_ROOT).append("/").append(
            CmsVfsConfiguration.N_VFS).append("/").append(CmsVfsConfiguration.N_RESOURCES).append("/").append(
                CmsVfsConfiguration.N_RESOURCELOADERS).toString();
    }

    /**
     * @see org.opencms.setup.xml.A_CmsSetupXmlUpdate#getXPathsToUpdate()
     */
    @Override
    protected List<String> getXPathsToUpdate() {

        if (m_xpaths == null) {
            // "/opencms/vfs/resources/resourceloaders/loader[@class='org.opencms.loader.CmsImageLoader']";
            StringBuffer xp = new StringBuffer(256);
            xp.append("/").append(CmsConfigurationManager.N_ROOT);
            xp.append("/").append(CmsVfsConfiguration.N_VFS);
            xp.append("/").append(CmsVfsConfiguration.N_RESOURCES);
            xp.append("/").append(CmsVfsConfiguration.N_RESOURCELOADERS);
            xp.append("/").append(CmsVfsConfiguration.N_LOADER);
            xp.append("[@").append(I_CmsXmlConfiguration.A_CLASS);
            xp.append("='");
            m_xpaths = new ArrayList<String>();
            for (int i = 0; i < LOADER_CLASSES.length; i++) {
                m_xpaths.add(xp.toString() + LOADER_CLASSES[i].getName() + "']");
            }
        }
        return m_xpaths;
    }

}