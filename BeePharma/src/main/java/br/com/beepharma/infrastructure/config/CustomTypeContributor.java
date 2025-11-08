package br.com.beepharma.infrastructure.config;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.hibernate.type.descriptor.jdbc.BasicExtractor;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;
import org.hibernate.type.descriptor.sql.internal.CapacityDependentDdlType;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

public class CustomTypeContributor implements TypeContributor {
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration()
                .getDdlTypeRegistry();
        final JdbcTypeRegistry jdbcTypeRegistry = typeContributions.getTypeConfiguration()
                .getJdbcTypeRegistry();

        jdbcTypeRegistry.addDescriptor(Types.CHAR, new JdbcType() {
            @Override
            public int getJdbcTypeCode() {
                return Types.CHAR;
            }

            @Override
            public <X> ValueBinder<X> getBinder(JavaType<X> javaType) {
                return new BasicBinder<X>(javaType, this) {
                    @Override
                    protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
                        if (value == null) {
                            st.setNull(index, Types.CHAR);
                        } else {
                            st.setString(index, value.toString());
                        }
                    }

                    @Override
                    protected void doBind(CallableStatement st, X value, String name, WrapperOptions options) throws SQLException {
                        if (value == null) {
                            st.setNull(name, Types.CHAR);
                        } else {
                            st.setString(name, value.toString());
                        }
                    }
                };
            }

            @SuppressWarnings("unchecked")
            private <X> X convertToUuid(String value, JavaType<X> javaType) {
                if (value == null) {
                    return null;
                }
                if (!UUID.class.equals(javaType.getJavaTypeClass())) {
                    throw new IllegalArgumentException("Unexpected java type: " + javaType.getJavaTypeClass());
                }
                return (X) UUID.fromString(value.trim());
            }

            @Override
            public <X> ValueExtractor<X> getExtractor(JavaType<X> javaType) {
                return new BasicExtractor<X>(javaType, this) {
                    @Override
                    protected X doExtract(ResultSet rs, int position, WrapperOptions options) throws SQLException {
                        String value = rs.getString(position);
                        return convertToUuid(value, javaType);
                    }

                    @Override
                    protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                        String value = statement.getString(index);
                        return convertToUuid(value, javaType);
                    }

                    @Override
                    protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                        String value = statement.getString(name);
                        return convertToUuid(value, javaType);
                    }
                };
            }

            @Override
            public String toString() {
                return "CharUuidType";
            }
        });

        ddlTypeRegistry.addDescriptor(Types.CHAR, 
            CapacityDependentDdlType.builder(Types.CHAR, "CHAR(36)", null)
                .build());
    }
}