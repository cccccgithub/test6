package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MemberDao {

    void addMember(Member member);

    Integer findMemberCountBeforeDate(String month);

    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);

    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);

    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();

    List<Map<String,Object>> getMemberSexReport();


    List<Date> findBirthday();
}
