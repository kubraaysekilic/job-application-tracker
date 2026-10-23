export const statuses={APPLIED:'Başvuruldu',INTERVIEW:'Görüşme',OFFER:'Teklif',REJECTED:'Olumsuz'} as const;
export type Status=keyof typeof statuses;
export type Application={id:number;company:string;position:string;url:string;appliedOn:string;status:Status;notes:string;version:number;createdAt:string};
export type Draft=Omit<Application,'id'|'version'|'createdAt'>;
export function localDate(){const d=new Date();return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`;}
export function filterApplications(items:Application[],query:string,status:string){const q=query.trim().toLocaleLowerCase('tr');return items.filter(a=>(status==='ALL'||a.status===status)&&`${a.company} ${a.position}`.toLocaleLowerCase('tr').includes(q));}
export function statistics(items:Application[]){return {total:items.length,interviews:items.filter(a=>a.status==='INTERVIEW').length,offers:items.filter(a=>a.status==='OFFER').length,active:items.filter(a=>a.status==='APPLIED'||a.status==='INTERVIEW').length};}
export async function request<T>(path:string,options?:RequestInit):Promise<T>{const res=await fetch('/api'+path,{...options,headers:{'Content-Type':'application/json',...options?.headers}});if(!res.ok){const error=await res.json().catch(()=>({message:'Sunucuya ulaşılamadı.'}));throw new Error(error.message||`İşlem başarısız (${res.status}).`);}return res.status===204?undefined as T:res.json();}
