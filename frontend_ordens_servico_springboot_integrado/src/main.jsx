import React, { useCallback, useEffect, useMemo, useState } from 'react'
import { createRoot } from 'react-dom/client'
import {
  Activity, AlertCircle, Building2, Check, ChevronDown, ClipboardList,
  Clock3, HardDrive, LayoutDashboard, LoaderCircle, Menu, Plus, RefreshCw,
  Search, Settings2, Wrench, X
} from 'lucide-react'
import './styles.css'

const API = '/api'
const dateFmt = (value) => {
  if (!value) return '—'
  const normalized = String(value).includes('T') ? value : String(value).replace(' ', 'T')
  const date = new Date(normalized)
  return Number.isNaN(date.getTime()) ? String(value) : date.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' })
}
const getOSId = (os) => os?.ordemServicoId ?? os?.id ?? '—'
const getEquipId = (eq) => eq?.id ?? '—'
const getSetorName = (eq) => eq?.setor?.nome ?? eq?.setorNome ?? 'Sem setor'

async function request(path, options = {}) {
  const response = await fetch(`${API}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  })
  if (!response.ok) {
    let message = `Erro ${response.status}`
    try {
      const data = await response.json()
      if (data && typeof data === 'object') message = Object.values(data).join(' · ') || message
    } catch { /* resposta sem corpo JSON */ }
    throw new Error(message)
  }
  if (response.status === 204) return null
  const text = await response.text()
  return text ? JSON.parse(text) : null
}

function App() {
  const [page, setPage] = useState('dashboard')
  const [mobileNav, setMobileNav] = useState(false)
  const [ordens, setOrdens] = useState([])
  const [equipamentos, setEquipamentos] = useState([])
  const [setores, setSetores] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  const [search, setSearch] = useState('')
  const [modal, setModal] = useState('')
  const [editing, setEditing] = useState(null)
  const [saving, setSaving] = useState(false)

  const refresh = useCallback(async () => {
    setLoading(true)
    setError('')
    try {
      const [o, e, s] = await Promise.all([
        request('/ordem-servicos'), request('/equipamentos'), request('/setor')
      ])
      setOrdens(Array.isArray(o) ? o : [])
      setEquipamentos(Array.isArray(e) ? e : [])
      setSetores(Array.isArray(s) ? s : [])
    } catch (err) {
      setError(`${err.message}. Confira se o back-end está executando em http://localhost:9091 e se o banco MySQL está disponível.`)
    } finally { setLoading(false) }
  }, [])

  useEffect(() => { refresh() }, [refresh])
  useEffect(() => {
    if (!notice) return
    const id = setTimeout(() => setNotice(''), 4000)
    return () => clearTimeout(id)
  }, [notice])

  const filteredEquipamentos = useMemo(() => equipamentos.filter((item) => {
    const term = search.toLowerCase()
    return `${item.nome || ''} ${item.numeroPatrimonio || ''} ${getSetorName(item)}`.toLowerCase().includes(term)
  }), [equipamentos, search])
  const filteredOrdens = useMemo(() => ordens.filter((item) => {
    const term = search.toLowerCase()
    return `${getOSId(item)} ${item.descricao || ''} ${item.equipamento?.nome || ''} ${item.equipamento?.numeroPatrimonio || ''}`.toLowerCase().includes(term)
  }), [ordens, search])

  async function submitForm(event) {
    event.preventDefault()
    const form = new FormData(event.currentTarget)
    setSaving(true)
    try {
      if (modal === 'ordem') {
        await request('/ordem-servicos', { method: 'POST', body: JSON.stringify({
          equipamentoId: Number(form.get('equipamentoId')),
          descricao: String(form.get('descricao')).trim(),
        }) })
        setNotice('Ordem de serviço aberta com sucesso.')
      } else if (modal === 'equipamento') {
        const payload = {
          nome: String(form.get('nome')).trim(),
          numeroPatrimonio: String(form.get('numeroPatrimonio')).trim(),
          setorId: Number(form.get('setorId')),
        }
        await request(editing ? `/equipamentos/${editing.id}` : '/equipamentos', {
          method: editing ? 'PUT' : 'POST', body: JSON.stringify(payload)
        })
        setNotice(editing ? 'Equipamento atualizado com sucesso.' : 'Equipamento cadastrado com sucesso.')
      } else if (modal === 'setor') {
        const payload = { nome: String(form.get('nome')).trim() }
        await request(editing ? `/setor/${editing.id}` : '/setor', {
          method: editing ? 'PUT' : 'POST', body: JSON.stringify(payload)
        })
        setNotice(editing ? 'Setor atualizado com sucesso.' : 'Setor cadastrado com sucesso.')
      }
      setEditing(null)
      setModal('')
      await refresh()
    } catch (err) {
      setNotice(`Não foi possível salvar: ${err.message}`)
    } finally { setSaving(false) }
  }

  async function deleteItem(type, item) {
    const label = type === 'setor' ? item.nome : `${item.nome} (${item.numeroPatrimonio})`
    if (!window.confirm(`Deseja realmente excluir ${label}?`)) return
    try {
      await request(`/${type === 'setor' ? 'setor' : 'equipamentos'}/${item.id}`, { method: 'DELETE' })
      setNotice('Registro excluído com sucesso.')
      await refresh()
    } catch (err) { setNotice(`Não foi possível excluir: ${err.message}`) }
  }

  const titles = {
    dashboard: ['Visão geral', 'Acompanhe as ordens de serviço e os ativos cadastrados.'],
    ordens: ['Ordens de serviço', 'Consulte e abra solicitações de manutenção.'],
    equipamentos: ['Equipamentos', 'Gerencie equipamentos e números de patrimônio.'],
    setores: ['Setores', 'Organize os locais e departamentos da sua operação.'],
  }
  const current = titles[page]

  return <div className="app-shell">
    <aside className={`sidebar ${mobileNav ? 'sidebar-open' : ''}`}>
      <div className="brand">
        <div className="brand-mark"><Wrench size={21}/></div>
        <div><strong>Ordem<span>Fácil</span></strong><small>GESTÃO DE MANUTENÇÃO</small></div>
        <button className="icon-button mobile-close" onClick={() => setMobileNav(false)} aria-label="Fechar menu"><X size={19}/></button>
      </div>
      <div className="workspace"><div className="workspace-icon"><Building2 size={17}/></div><div><strong>Operação principal</strong><small>Área de trabalho</small></div><ChevronDown size={15}/></div>
      <div className="nav-label">MENU PRINCIPAL</div>
      <nav className="nav-list">
        <NavItem icon={<LayoutDashboard size={18}/>} label="Visão geral" active={page === 'dashboard'} onClick={() => {setPage('dashboard');setMobileNav(false)}}/>
        <NavItem icon={<ClipboardList size={18}/>} label="Ordens de serviço" count={ordens.length} active={page === 'ordens'} onClick={() => {setPage('ordens');setSearch('');setMobileNav(false)}}/>
        <NavItem icon={<HardDrive size={18}/>} label="Equipamentos" active={page === 'equipamentos'} onClick={() => {setPage('equipamentos');setSearch('');setMobileNav(false)}}/>
        <NavItem icon={<Building2 size={18}/>} label="Setores" active={page === 'setores'} onClick={() => {setPage('setores');setSearch('');setMobileNav(false)}}/>
      </nav>
      <div className="sidebar-bottom"><div className="online-dot"/><div><strong>API conectada via proxy</strong><small>Back-end · porta 9091</small></div></div>
    </aside>

    {mobileNav && <button className="nav-scrim" onClick={() => setMobileNav(false)} aria-label="Fechar menu"/>}
    <main className="main-area">
      <header className="topbar">
        <button className="icon-button mobile-menu" onClick={() => setMobileNav(true)} aria-label="Abrir menu"><Menu size={21}/></button>
        <div className="breadcrumb">Workspace <span>/</span> <strong>{current[0]}</strong></div>
        <div className="topbar-right"><span className="status-pill"><span/> Sistema de manutenção</span><div className="avatar">OS</div></div>
      </header>
      <section className="content">
        <div className="page-heading"><div><div className="eyebrow">CENTRAL DE OPERAÇÕES</div><h1>{current[0]}</h1><p>{current[1]}</p></div>
          <button className="btn btn-secondary refresh-btn" onClick={refresh} disabled={loading}><RefreshCw size={16} className={loading ? 'spin' : ''}/> Atualizar</button>
        </div>

        {error && <div className="alert alert-error"><AlertCircle size={19}/><div><strong>Não foi possível carregar os dados</strong><p>{error}</p></div><button className="icon-button" onClick={() => setError('')}><X size={17}/></button></div>}
        {notice && <div className="toast"><Check size={17}/>{notice}<button className="icon-button" onClick={() => setNotice('')}><X size={16}/></button></div>}

        {page === 'dashboard' && <Dashboard ordens={ordens} equipamentos={equipamentos} setores={setores} loading={loading} onNavigate={setPage} onNew={() => setModal('ordem')}/>}
        {page === 'ordens' && <section className="panel">
          <PanelHeader title="Todas as ordens" subtitle={`${ordens.length} registro(s) no sistema`} action={<button className="btn btn-primary" onClick={() => setModal('ordem')}><Plus size={17}/> Nova ordem</button>}/>
          <SearchBox value={search} onChange={setSearch} placeholder="Buscar por ordem, descrição ou equipamento..."/>
          <OrdersTable ordens={filteredOrdens} loading={loading}/>
        </section>}
        {page === 'equipamentos' && <section className="panel">
          <PanelHeader title="Equipamentos cadastrados" subtitle={`${equipamentos.length} ativo(s) no sistema`} action={<button className="btn btn-primary" onClick={() => setModal('equipamento')}><Plus size={17}/> Novo equipamento</button>}/>
          <SearchBox value={search} onChange={setSearch} placeholder="Buscar equipamento, patrimônio ou setor..."/>
          <div className="table-wrap"><table><thead><tr><th>Equipamento</th><th>Nº patrimônio</th><th>Setor</th><th>ID</th><th>Ações</th></tr></thead><tbody>
            {loading ? <EmptyRow colSpan={5} text="Carregando equipamentos..."/> : filteredEquipamentos.length ? filteredEquipamentos.map(eq => <tr key={eq.id}><td><div className="cell-main"><div className="table-icon blue"><HardDrive size={17}/></div><strong>{eq.nome}</strong></div></td><td><span className="code-chip">{eq.numeroPatrimonio || '—'}</span></td><td>{getSetorName(eq)}</td><td className="muted">#{getEquipId(eq)}</td><td><div className="row-actions"><button className="table-action" onClick={() => {setEditing(eq);setModal('equipamento')}} title="Editar equipamento"><Settings2 size={15}/> Editar</button><button className="table-action danger" onClick={() => deleteItem('equipamento', eq)} title="Excluir equipamento"><X size={16}/> Excluir</button></div></td></tr>) : <EmptyRow colSpan={5} text="Nenhum equipamento encontrado."/>}
          </tbody></table></div>
        </section>}
        {page === 'setores' && <section className="panel">
          <PanelHeader title="Setores cadastrados" subtitle={`${setores.length} setor(es) no sistema`} action={<button className="btn btn-primary" onClick={() => {setEditing(null);setModal('setor')}}><Plus size={17}/> Novo setor</button>}/>
          <SearchBox value={search} onChange={setSearch} placeholder="Buscar setor..."/>
          <div className="table-wrap"><table><thead><tr><th>Setor</th><th>ID</th><th>Equipamentos</th><th>Ações</th></tr></thead><tbody>
            {loading ? <EmptyRow colSpan={4} text="Carregando setores..."/> : setores.filter(s => s.nome?.toLowerCase().includes(search.toLowerCase())).length ? setores.filter(s => s.nome?.toLowerCase().includes(search.toLowerCase())).map(s => <tr key={s.id}><td><div className="cell-main"><div className="table-icon violet"><Building2 size={17}/></div><strong>{s.nome}</strong></div></td><td className="muted">#{s.id}</td><td>{equipamentos.filter(e => e.setor?.id === s.id).length}</td><td><div className="row-actions"><button className="table-action" onClick={() => {setEditing(s);setModal('setor')}} title="Editar setor"><Settings2 size={15}/> Editar</button><button className="table-action danger" onClick={() => deleteItem('setor', s)} title="Excluir setor"><X size={16}/> Excluir</button></div></td></tr>) : <EmptyRow colSpan={4} text="Nenhum setor encontrado."/>}
          </tbody></table></div>
        </section>}
        <footer className="footer"><span>OrdemFácil <span className="footer-dot">•</span> Gestão de manutenção</span><span><span className="online-dot"/> Conecte sua API para atualizar os dados</span></footer>
      </section>
    </main>

    {modal && <Modal title={modal === 'ordem' ? 'Abrir ordem de serviço' : modal === 'equipamento' ? (editing ? 'Editar equipamento' : 'Cadastrar equipamento') : (editing ? 'Editar setor' : 'Cadastrar setor')} subtitle={modal === 'ordem' ? 'Descreva a solicitação e selecione o equipamento.' : modal === 'equipamento' ? 'Informe os dados do ativo e o setor responsável.' : 'Informe o nome do setor.'} onClose={() => {setModal('');setEditing(null)}}>
      <form onSubmit={submitForm} className="form">
        {modal === 'ordem' && <>
          <label>Equipamento <span>*</span><select name="equipamentoId" required defaultValue=""><option value="" disabled>Selecione um equipamento</option>{equipamentos.map(eq => <option key={eq.id} value={eq.id}>{eq.nome} · Patrimônio {eq.numeroPatrimonio}</option>)}</select></label>
          <label>Descrição do problema ou serviço <span>*</span><textarea name="descricao" rows="4" required minLength="2" placeholder="Descreva o que precisa ser verificado ou reparado..."/></label>
          {!equipamentos.length && <p className="form-hint"><AlertCircle size={15}/> Cadastre um equipamento antes de abrir uma ordem.</p>}
        </>}
        {modal === 'equipamento' && <>
          <label>Nome do equipamento <span>*</span><input name="nome" required defaultValue={editing?.nome || ''} placeholder="Ex.: Ar-condicionado sala 12"/></label>
          <label>Número do patrimônio <span>*</span><input name="numeroPatrimonio" required defaultValue={editing?.numeroPatrimonio || ''} placeholder="Ex.: PAT-00125"/></label>
          <label>Setor responsável <span>*</span><select name="setorId" required defaultValue={editing?.setor?.id ?? ''}><option value="" disabled>Selecione um setor</option>{setores.map(s => <option key={s.id} value={s.id}>{s.nome}</option>)}</select></label>
          {!setores.length && <p className="form-hint"><AlertCircle size={15}/> Cadastre um setor antes de cadastrar um equipamento.</p>}
        </>}
        {modal === 'setor' && <label>Nome do setor <span>*</span><input name="nome" required defaultValue={editing?.nome || ''} placeholder="Ex.: Tecnologia da Informação"/></label>}
        <div className="modal-actions"><button type="button" className="btn btn-secondary" onClick={() => {setModal('');setEditing(null)}}>Cancelar</button><button type="submit" className="btn btn-primary" disabled={saving || (modal === 'ordem' && !equipamentos.length) || (modal === 'equipamento' && !setores.length)}>{saving ? <LoaderCircle size={16} className="spin"/> : <Check size={16}/>} {saving ? 'Salvando...' : 'Salvar registro'}</button></div>
      </form>
    </Modal>}
  </div>
}

function NavItem({ icon, label, active, count, onClick }) {
  return <button className={`nav-item ${active ? 'active' : ''}`} onClick={onClick}>{icon}<span>{label}</span>{count !== undefined && <span className="nav-count">{count}</span>}</button>
}
function PanelHeader({ title, subtitle, action }) {
  return <div className="panel-header"><div><h2>{title}</h2><p>{subtitle}</p></div>{action}</div>
}
function SearchBox({ value, onChange, placeholder }) {
  return <div className="search-box"><Search size={17}/><input value={value} onChange={e => onChange(e.target.value)} placeholder={placeholder}/>{value && <button onClick={() => onChange('')} aria-label="Limpar busca"><X size={15}/></button>}</div>
}
function EmptyRow({ colSpan, text }) { return <tr><td colSpan={colSpan}><div className="empty-state"><ClipboardList size={25}/><span>{text}</span></div></td></tr> }
function OrdersTable({ ordens, loading }) {
  return <div className="table-wrap"><table><thead><tr><th>Ordem</th><th>Descrição</th><th>Equipamento</th><th>Patrimônio</th><th>Data de abertura</th></tr></thead><tbody>
    {loading ? <EmptyRow colSpan={5} text="Carregando ordens de serviço..."/> : ordens.length ? [...ordens].sort((a,b) => Number(getOSId(b))-Number(getOSId(a))).map(os => <tr key={getOSId(os)}><td><span className="order-number">OS-{String(getOSId(os)).padStart(4,'0')}</span></td><td><div className="description-cell">{os.descricao || 'Sem descrição'}</div></td><td><div className="cell-main"><div className="table-icon blue"><HardDrive size={16}/></div><span>{os.equipamento?.nome || '—'}</span></div></td><td><span className="code-chip">{os.equipamento?.numeroPatrimonio || '—'}</span></td><td><span className="date-cell"><Clock3 size={14}/>{dateFmt(os.dataAbertura)}</span></td></tr>) : <EmptyRow colSpan={5} text="Nenhuma ordem de serviço encontrada."/>}
  </tbody></table></div>
}
function Dashboard({ ordens, equipamentos, setores, loading, onNavigate, onNew }) {
  const recent = [...ordens].sort((a,b) => Number(getOSId(b))-Number(getOSId(a))).slice(0,5)
  return <>
    <div className="welcome-banner"><div className="welcome-copy"><div className="welcome-tag"><Activity size={14}/> PAINEL OPERACIONAL</div><h2>Olá! Vamos manter tudo<br/>funcionando bem.</h2><p>Acompanhe os ativos e centralize as solicitações de manutenção em um só lugar.</p><button className="btn btn-light" onClick={onNew}><Plus size={17}/> Abrir ordem de serviço</button></div><div className="banner-art"><div className="art-ring ring-one"/><div className="art-ring ring-two"/><div className="art-card"><Wrench size={38}/><span>MANUTENÇÃO</span><strong>Em dia, sempre.</strong></div><div className="art-bubble bubble-one"><Check size={16}/></div><div className="art-bubble bubble-two"><Settings2 size={17}/></div></div></div>
    <div className="section-heading"><div><h2>Resumo da operação</h2><p>Indicadores gerais do seu sistema</p></div><span className="updated-label"><span/> Dados da API</span></div>
    <div className="stats-grid">
      <StatCard icon={<ClipboardList size={19}/>} tone="blue" label="Ordens de serviço" value={ordens.length} note="Solicitações registradas" onClick={() => onNavigate('ordens')}/>
      <StatCard icon={<HardDrive size={19}/>} tone="green" label="Equipamentos" value={equipamentos.length} note="Ativos cadastrados" onClick={() => onNavigate('equipamentos')}/>
      <StatCard icon={<Building2 size={19}/>} tone="purple" label="Setores" value={setores.length} note="Áreas cadastradas" onClick={() => onNavigate('setores')}/>
      <StatCard icon={<Clock3 size={19}/>} tone="orange" label="Ordens recentes" value={recent.length} note="Últimos registros listados" onClick={() => onNavigate('ordens')}/>
    </div>
    <section className="panel recent-panel"><PanelHeader title="Ordens recentes" subtitle="Últimas ordens registradas no sistema" action={<button className="text-button" onClick={() => onNavigate('ordens')}>Ver todas <span>→</span></button>}/><OrdersTable ordens={recent} loading={loading}/></section>
    <div className="quick-grid"><button className="quick-card" onClick={() => onNavigate('equipamentos')}><div className="quick-icon blue"><HardDrive size={20}/></div><div><strong>Gerenciar equipamentos</strong><p>Consulte os ativos e seus patrimônios.</p></div><span className="quick-arrow">↗</span></button><button className="quick-card" onClick={() => onNavigate('setores')}><div className="quick-icon purple"><Building2 size={20}/></div><div><strong>Organizar setores</strong><p>Mantenha os setores atualizados.</p></div><span className="quick-arrow">↗</span></button></div>
    {loading && <div className="loading-note"><LoaderCircle size={15} className="spin"/> Atualizando indicadores…</div>}
  </>
}
function StatCard({ icon, tone, label, value, note, onClick }) {
  return <button className="stat-card" onClick={onClick}><div className={`stat-icon ${tone}`}>{icon}<span className="stat-arrow">↗</span></div><div className="stat-label">{label}</div><div className="stat-value">{value}</div><div className="stat-note">{note}</div></button>
}
function Modal({ title, subtitle, onClose, children }) {
  useEffect(() => {
    const listener = e => e.key === 'Escape' && onClose()
    window.addEventListener('keydown', listener)
    return () => window.removeEventListener('keydown', listener)
  }, [onClose])
  return <div className="modal-backdrop" onMouseDown={e => e.target === e.currentTarget && onClose()}><section className="modal"><div className="modal-head"><div><h2>{title}</h2><p>{subtitle}</p></div><button className="icon-button" onClick={onClose} aria-label="Fechar"><X size={19}/></button></div>{children}</section></div>
}

createRoot(document.getElementById('root')).render(<React.StrictMode><App/></React.StrictMode>)
