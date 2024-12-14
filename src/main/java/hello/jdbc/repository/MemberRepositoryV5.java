package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC 템플릿 사용
 **/
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id,money) values (?, ?)";
        template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    public Member findById(String memberId) {
        String sql = "select * from member where member_id= ?";
        //한건 조회 == ForObject
        //두번째 파라미터는 쿼리 결과 객체를 어떻게 만들것인지
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql,money,memberId);
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql,memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs,rowNum) -> {
            Member member=new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
