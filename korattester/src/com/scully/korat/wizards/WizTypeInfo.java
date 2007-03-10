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
    
    private Map<String, List<StateFieldDTO>> objectFields;

    public WizTypeInfo(IType type)
    {
        this.type = type;
        
        // initialize field data
        this.primitiveFields = new HashMap<String, List<StateFieldDTO>>();
        this.objectFields = new HashMap<String, List<StateFieldDTO>>();
        this.usedTypes = new HashSet<String>();
        try
        {
            this.collectTypes(this.type.getFields());
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            this.primitiveFields = null;
	        this.objectFields = null;
            e.printStackTrace();
        }
    }

    private void putPrimitiveField(IField field) throws IllegalArgumentException, JavaModelException
    {
        String typeName = Signature.toString(field.getTypeSignature());
        putField(this.primitiveFields, field, typeName);
    }
    
    private void putObjectField(IField field) throws IllegalArgumentException, JavaModelException
    {
        String typeName = findType(field).getFullyQualifiedName();
        putField(this.objectFields, field, typeName);
    }
    
    
    private static void putField(Map<String, List<StateFieldDTO>> fieldMap, IField field, String typeName) throws IllegalArgumentException, JavaModelException
    {
        String parentType = field.getDeclaringType().getFullyQualifiedName();
        String fieldName = field.getElementName();

        StateFieldDTO fieldDTO = new StateFieldDTO();
        fieldDTO.setParentClass(parentType);
        fieldDTO.setName(fieldName);
        fieldDTO.setType(typeName);
        List<StateFieldDTO> fields = fieldMap.get(parentType);
        if (fields == null)
        {
            fields = new ArrayList<StateFieldDTO>();
        }
        fields.add(fieldDTO);
        fieldMap.put(parentType, fields);
        
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
                    putObjectField(field);
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

    /**
     * @return the objectFields
     */
    public Map<String, List<StateFieldDTO>> getObjectFields()
    {
        return objectFields;
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

}
