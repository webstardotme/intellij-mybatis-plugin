package com.seventh7.mybatis.locator;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.search.GlobalSearchScope;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yanglin
 */
public class MapperXmlPakcageProvider extends PackageProvider{

  @NotNull @Override
  public Set<PsiPackage> getPackages(@NotNull Project project) {
    HashSet<PsiPackage> res = Sets.newHashSet();
    Collection<Mapper> mappers = MapperUtils.findMappers(project);
    JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
    for (Mapper mapper : mappers) {
      String namespace = MapperUtils.getNamespace(mapper);
      PsiClass clzz = javaPsiFacade.findClass(namespace, GlobalSearchScope.allScope(project));
      if (null != clzz) {
        PsiFile file = clzz.getContainingFile();
        if (file instanceof PsiJavaFile) {
          String packageName = ((PsiJavaFile) file).getPackageName();
          PsiPackage pkg = javaPsiFacade.findPackage(packageName);
          if (null != pkg) {
            res.add(pkg);
          }
        }
      }
    }
    return res;
  }

}
