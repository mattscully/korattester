package com.scully.korat.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import com.scully.korat.map.StateFieldDTO;

public class WizTypeInfo
{
    private IType type;

    private Set<String> usedTypes;

    private Map<String, List<StateFieldDTO>> primitiveFields;

    public WizTypeInfo(IType type)
    {
        this.type = type;
        
        // initialize field data
        this.primitiveFields = new HashMap<String, List<StateFieldDTO>>();
        this.usedTypes = new HashSet<String>();
        try
        {
            this.collectTypes(this.type.getFields());
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            this.primitiveFields = null;
            e.printStackTrace();
        }
    }

    public Map<String, List<StateFieldDTO>> getPrimitiveFields()
    {
        return this.primitiveFields;
    }

    /**
     * 
     */
    public Set<String> getUsedTypes()
    {
        return this.usedTypes;
    }

    private void putPrimitiveField(IField field) throws IllegalArgumentException, JavaModelException
    {
        String parentType = field.getDeclaringType().getFullyQualifiedName();
        String fieldName = field.getElementName();
        String typeName = Signature.toString(field.getTypeSignature());

        StateFieldDTO fieldDTO = new StateFieldDTO();
        fieldDTO.setParentClass(parentType);
        fieldDTO.setName(fieldName);
        fieldDTO.setType(typeName);
        List<StateFieldDTO> fields = this.primitiveFields.get(parentType);
        if (fields == null)
        {
            fields = new ArrayList<StateFieldDTO>();
        }
        fields.add(fieldDTO);
        this.primitiveFields.put(parentType, fields);
    }

    private void collectTypes(IField[] fields) throws JavaModelException
    {
        if (fields != null)
        {
            for (IField field : fields)
            {
                IType fieldType = findType(field);
                // type of Object
                if (fieldType != null)
                {
                    String qualifiedName = fieldType.getFullyQualifiedName();
                    if (StringUtils.isNotEmpty(qualifiedName) && !this.usedTypes.contains(qualifiedName))
                    {
                        this.usedTypes.add(qualifiedName);
                        collectTypes(fieldType.getFields());
                    }
                }
                // primitive type
                else
                {
                    putPrimitiveField(field);
                }
            }
        }
    }

    /**
     * Return the IType of the specified field
     * @param field
     * @return
     * @throws JavaModelException
     */
    private IType findType(IField field) throws JavaModelException
    {
        String fieldTypeName = Signature.toString(field.getTypeSignature());
        String[][] qualifiedName = this.type.resolveType(fieldTypeName);
        if (qualifiedName != null)
        {
            // type of Object
            IJavaProject project = (IJavaProject) this.type.getJavaProject();
            return project.findType(qualifiedName[0][0], qualifiedName[0][1], new NullProgressMonitor());
        }
        else
        {
            // primitive type
            return null;
        }
    }

    /**
     * @return the type
     */
    public IType getType()
    {
        return type;
    }

}
