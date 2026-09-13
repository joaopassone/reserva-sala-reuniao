-- 1. Carga de Usuários (Senha em texto plano: senha123)
INSERT INTO tb_usuarios (id, nome, email, senha, tipo) VALUES 
('u1111111-1111-1111-1111-111111111111', 'Admin Tech Challenge', 'joao.passone@gmail.com', '$2a$10$b9Tm5Iw4v4cEbyX89KV1rOBZ1Cm6Yke9QuXuAPhmz6vaLFAXC81tK', 'ADMIN'),
('u2222222-2222-2222-2222-222222222222', 'Colaborador Comum', 'usuario@email.com', '$2a$10$b9Tm5Iw4v4cEbyX89KV1rOBZ1Cm6Yke9QuXuAPhmz6vaLFAXC81tK', 'COMUM');

-- 2. Carga de Recursos Base
INSERT INTO tb_recursos (id, nome) VALUES 
('r1111111-1111-1111-1111-111111111111', 'Projetor 4K'),
('r2222222-2222-2222-2222-222222222222', 'Ar Condicionado'),
('r3333333-3333-3333-3333-333333333333', 'Sistema de Videoconferência Polycom'),
('r4444444-4444-4444-4444-444444444444', 'Quadro Branco');

-- 3. Carga de Salas de Reunião
INSERT INTO tb_salas (id, nome, capacidade, localizacao) VALUES 
('s1111111-1111-1111-1111-111111111111', 'Sala Alpha Premium', 12, 'Bloco B, Cobertura'),
('s2222222-2222-2222-2222-222222222222', 'Auditório Principal', 150, 'Bloco A, Térreo'),
('s3333333-3333-3333-3333-333333333333', 'Sala de Brainstorming', 6, 'Bloco B, Sala 104');

-- 4. Vinculo de Recursos nas Salas (Muitos para Muitos)
-- Sala Alpha Premium tem Projetor, Ar e Videoconferência
INSERT INTO tb_salas_recursos (sala_id, recurso_id) VALUES 
('s1111111-1111-1111-1111-111111111111', 'r1111111-1111-1111-1111-111111111111'),
('s1111111-1111-1111-1111-111111111111', 'r2222222-2222-2222-2222-222222222222'),
('s1111111-1111-1111-1111-111111111111', 'r3333333-3333-3333-3333-333333333333');

-- Auditório tem Projetor e Ar
INSERT INTO tb_salas_recursos (sala_id, recurso_id) VALUES 
('s2222222-2222-2222-2222-222222222222', 'r1111111-1111-1111-1111-111111111111'),
('s2222222-2222-2222-2222-222222222222', 'r2222222-2222-2222-2222-222222222222');

-- Sala de Brainstorming tem Quadro Branco e Ar
INSERT INTO tb_salas_recursos (sala_id, recurso_id) VALUES 
('s3333333-3333-3333-3333-333333333333', 'r4444444-4444-4444-4444-444444444444'),
('s3333333-3333-3333-3333-333333333333', 'r2222222-2222-2222-2222-222222222222');
