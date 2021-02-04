import React, { Component } from 'react';
import Axios from 'axios';
import Ranger from './../../images/ranger.png';
import Emmett from './../../images/emmett.png';
import LadyLiberty from './../../images/ladyliberty.png';
import Shakespeare from './../../images/shakespeare.png';

class Home extends Component {
    state = {
        addStatus: '',
        customer: {},
        wantList: [],
        addPart: {
            partID: '',
            myCustomer: '',
            type: '',
            number: '',
            name: '',
            colorNum: '',
            cond: '',
            quantity: '',
            matchingVendors: []
        },
        selectedPart: {
            partID: '',
            myCustomer: '',
            type: '',
            number: '',
            name: '',
            colorNum: '',
            cond: '',
            quantity: '',
            matchingVendors: []
        }
    }
    componentDidMount() {
        const email = localStorage.getItem("loggedInUser");
        const params = { email };
        Axios.get('http://localhost:4500/brickbuddy/findCustomerByEmail', { params })
            .then(response => {
                this.setState(
                    {
                        customer: response.data
                    }
                )
            }).catch(error => {
                console.log('findCustomerByEmail ', error.response.status);
            })
        Axios.get('http://localhost:4500/brickbuddy/getCustomerWantList', { params })
            .then(response => {
                this.setState(
                    {
                        wantList: response.data
                    }
                )
                this.props.history.push('/home');
            }).catch(error => {
                console.log('getCustomerWantList ', error.response.status);
            })
    }
    handleClick = (wantedPart) => {
        this.setState(
            {
                selectedPart: wantedPart
            }
        )
    }
    handleRemove = () => {
        const data = this.state.selectedPart;
        Axios.post('http://localhost:4500/brickbuddy/removeWantedPart', data)
            .then(response => {
                this.setState(
                    {
                        wantList: response.data
                    }
                )
                // this.props.history.push('/home');
            }).catch(error => {
                console.log('handleRemove ', error.response.status);
                //  https://gist.github.com/fgilio/230ccd514e9381fafa51608fcf137253

            })
    }
    handleUpdate = () => {
        const data = this.state.selectedPart;
        Axios.post('http://localhost:4500/brickbuddy/updateWantedPart', data)
            .then(response => {
                this.setState(
                    {
                        wantList: response.data
                    }
                )
                // this.props.history.push('/home');
            }).catch(error => {
                console.log('handleUpdate ', error.response.status);
                //Display an error message  error.response.status will 
                //https://gist.github.com/fgilio/230ccd514e9381fafa51608fcf137253

            })

    }
    handleUpdateChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        const tempPart = { ...this.state.selectedPart }
        tempPart[name] = value;
        this.setState(
            {
                selectedPart: tempPart
            }
        )
    }
    handleAdd = () => {
        const tempPart = this.state.addPart;
        tempPart.myCustomer = this.state.customer;
        // This will continue as an asyncronous call, does not hang the UI waiting for data from backend.
        Axios.post('http://localhost:4500/brickbuddy/addWantedPart', this.state.addPart)
            .then(response => {
                this.setState(
                    {
                        addStatus: '',
                        wantList: response.data
                    }, () => this.props.history.push('/home')
                )
                // this.props.history.push('/home');
            }).catch(error => {
                console.log('handleAdd ', error.response.status);
                if (error.response.status == 411) {
                    this.setState(
                        {
                            addStatus: <p className="input-error back-blue">Part is not found. Try updating the color.</p>
                        }
                    )
                };
            });
    }
    handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        const tempPart = { ...this.state.addPart }
        tempPart[name] = value;
        this.setState(
            {
                addPart: tempPart
            }
        )
    }
    render() {
        return (
            <div className="fullscreen construction-bg">
                <div className="container-fluid head-foot-padding">
                    <div className="row">
                        <nav className="col-md-2 d-none d-sm-block brick-bg sidebar">
                            <div className="sidebar-sticky">
                                <img src={Ranger} height="350px" width="auto" />
                                <img src={Emmett} height="350px" width="auto" />
                                <img src={LadyLiberty} height="350px" width="auto" />
                                <img src={Shakespeare} height="350px" width="auto" />
                            </div>
                        </nav>

                        <main role="main" className="col-md-9 ml-sm-auto col-lg-10 px-4">
                            <h1 className="h2 back-blue text-gold mt-3">Add a part to my Want List</h1>
                            <form className="green-shade px-3 py-2">
                                <div className="row mb-2">
                                    <div className="col-md-3">
                                        <label for="type">Part Type</label>
                                        <select className="custom-select" onChange={this.handleChange} name="type"
                                            value={this.state.addPart.type} id="type" aria-label="Part type" required>
                                            <option value="">Select...</option>
                                            <option value="P">Lego Part</option>
                                            <option value="M">Minifigure</option>
                                        </select>
                                    </div>
                                    <div className="col-md-2">
                                        <label for="pnum">Part Number</label>
                                        <input type="text" onChange={this.handleChange} id="pnum" name="number"
                                            value={this.state.addPart.number} className="form-control" placeholder="Part number"
                                            aria-label="Part Number" required />
                                    </div>
                                    <div className="col-md-3">
                                        <label for="colorNum">Color *</label>
                                        <select className="custom-select" onChange={this.handleChange} name="colorNum"
                                            value={this.state.addPart.colorNum} id="colorNum" aria-label="How many do you want?" required>
                                            <option value="">Select...</option>
                                            <optgroup label="-- LEGO Colors">
                                                <option class="genColorList" data-colortype="1" value="41">Aqua</option>
                                                <option class="genColorList" data-colortype="1" value="11">Black</option>
                                                <option class="genColorList" data-colortype="1" value="7">Blue</option>
                                                <option class="genColorList" data-colortype="1" value="97">Blue-Violet</option>
                                                <option class="genColorList" data-colortype="1" value="36">Bright Green</option>
                                                <option class="genColorList" data-colortype="1" value="105">Bright Light Blue</option>
                                                <option class="genColorList" data-colortype="1" value="110">Bright Light Orange</option>
                                                <option class="genColorList" data-colortype="1" value="103">Bright Light Yellow</option>
                                                <option class="genColorList" data-colortype="1" value="104">Bright Pink</option>
                                                <option class="genColorList" data-colortype="1" value="8">Brown</option>
                                                <option class="genColorList" data-colortype="1" value="227">Clikits Lavender</option>
                                                <option class="genColorList" data-colortype="1" value="220">Coral</option>
                                                <option class="genColorList" data-colortype="1" value="153">Dark Azure</option>
                                                <option class="genColorList" data-colortype="1" value="63">Dark Blue</option>
                                                <option class="genColorList" data-colortype="1" value="109">Dark Blue-Violet</option>
                                                <option class="genColorList" data-colortype="1" value="85">Dark Bluish Gray</option>
                                                <option class="genColorList" data-colortype="1" value="120">Dark Brown</option>
                                                <option class="genColorList" data-colortype="1" value="10">Dark Gray</option>
                                                <option class="genColorList" data-colortype="1" value="80">Dark Green</option>
                                                <option class="genColorList" data-colortype="1" value="225">Dark Nougat</option>
                                                <option class="genColorList" data-colortype="1" value="68">Dark Orange</option>
                                                <option class="genColorList" data-colortype="1" value="47">Dark Pink</option>
                                                <option class="genColorList" data-colortype="1" value="89">Dark Purple</option>
                                                <option class="genColorList" data-colortype="1" value="59">Dark Red</option>
                                                <option class="genColorList" data-colortype="1" value="231">Dark Salmon</option>
                                                <option class="genColorList" data-colortype="1" value="69">Dark Tan</option>
                                                <option class="genColorList" data-colortype="1" value="39">Dark Turquoise</option>
                                                <option class="genColorList" data-colortype="1" value="161">Dark Yellow</option>
                                                <option class="genColorList" data-colortype="1" value="29">Earth Orange</option>
                                                <option class="genColorList" data-colortype="1" value="106">Fabuland Brown</option>
                                                <option class="genColorList" data-colortype="1" value="160">Fabuland Orange</option>
                                                <option class="genColorList" data-colortype="1" value="6">Green</option>
                                                <option class="genColorList" data-colortype="1" value="154">Lavender</option>
                                                <option class="genColorList" data-colortype="1" value="152">Light Aqua</option>
                                                <option class="genColorList" data-colortype="1" value="62">Light Blue</option>
                                                <option class="genColorList" data-colortype="1" value="86">Light Bluish Gray</option>
                                                <option class="genColorList" data-colortype="1" value="9">Light Gray</option>
                                                <option class="genColorList" data-colortype="1" value="38">Light Green</option>
                                                <option class="genColorList" data-colortype="1" value="35">Light Lime</option>
                                                <option class="genColorList" data-colortype="1" value="90">Light Nougat</option>
                                                <option class="genColorList" data-colortype="1" value="32">Light Orange</option>
                                                <option class="genColorList" data-colortype="1" value="56">Light Pink</option>
                                                <option class="genColorList" data-colortype="1" value="93">Light Purple</option>
                                                <option class="genColorList" data-colortype="1" value="26">Light Salmon</option>
                                                <option class="genColorList" data-colortype="1" value="40">Light Turquoise</option>
                                                <option class="genColorList" data-colortype="1" value="44">Light Violet</option>
                                                <option class="genColorList" data-colortype="1" value="33">Light Yellow</option>
                                                <option class="genColorList" data-colortype="1" value="34">Lime</option>
                                                <option class="genColorList" data-colortype="1" value="72">Maersk Blue</option>
                                                <option class="genColorList" data-colortype="1" value="71">Magenta</option>
                                                <option class="genColorList" data-colortype="1" value="156">Medium Azure</option>
                                                <option class="genColorList" data-colortype="1" value="42">Medium Blue</option>
                                                <option class="genColorList" data-colortype="1" value="91">Medium Brown</option>
                                                <option class="genColorList" data-colortype="1" value="94">Medium Dark Pink</option>
                                                <option class="genColorList" data-colortype="1" value="37">Medium Green</option>
                                                <option class="genColorList" data-colortype="1" value="157">Medium Lavender</option>
                                                <option class="genColorList" data-colortype="1" value="76">Medium Lime</option>
                                                <option class="genColorList" data-colortype="1" value="150">Medium Nougat</option>
                                                <option class="genColorList" data-colortype="1" value="31">Medium Orange</option>
                                                <option class="genColorList" data-colortype="1" value="73">Medium Violet</option>
                                                <option class="genColorList" data-colortype="1" value="166">Neon Green</option>
                                                <option class="genColorList" data-colortype="1" value="165">Neon Orange</option>
                                                <option class="genColorList" data-colortype="1" value="28">Nougat</option>
                                                <option class="genColorList" data-colortype="1" value="155">Olive Green</option>
                                                <option class="genColorList" data-colortype="1" value="4">Orange</option>
                                                <option class="genColorList" data-colortype="1" value="23">Pink</option>
                                                <option class="genColorList" data-colortype="1" value="24">Purple</option>
                                                <option class="genColorList" data-colortype="1" value="5">Red</option>
                                                <option class="genColorList" data-colortype="1" value="88">Reddish Brown</option>
                                                <option class="genColorList" data-colortype="1" value="27">Rust</option>
                                                <option class="genColorList" data-colortype="1" value="25">Salmon</option>
                                                <option class="genColorList" data-colortype="1" value="55">Sand Blue</option>
                                                <option class="genColorList" data-colortype="1" value="48">Sand Green</option>
                                                <option class="genColorList" data-colortype="1" value="54">Sand Purple</option>
                                                <option class="genColorList" data-colortype="1" value="58">Sand Red</option>
                                                <option class="genColorList" data-colortype="1" value="87">Sky Blue</option>
                                                <option class="genColorList" data-colortype="1" value="2">Tan</option>
                                                <option class="genColorList" data-colortype="1" value="99">Very Light Bluish Gray</option>
                                                <option class="genColorList" data-colortype="1" value="49">Very Light Gray</option>
                                                <option class="genColorList" data-colortype="1" value="96">Very Light Orange</option>
                                                <option class="genColorList" data-colortype="1" value="43">Violet</option>
                                                <option class="genColorList" data-colortype="1" value="1">White</option>
                                                <option class="genColorList" data-colortype="1" value="3">Yellow</option>
                                                <option class="genColorList" data-colortype="1" value="158">Yellowish Green</option>
                                            </optgroup>
                                            <optgroup class="genColorList" data-colortype="2" label="-- Transparent Colors">
                                                <option class="genColorList" data-colortype="2" value="113">Trans-Aqua</option>
                                                <option class="genColorList" data-colortype="2" value="13">Trans-Black</option>
                                                <option class="genColorList" data-colortype="2" value="108">Trans-Bright Green</option>
                                                <option class="genColorList" data-colortype="2" value="12">Trans-Clear</option>
                                                <option class="genColorList" data-colortype="2" value="14">Trans-Dark Blue</option>
                                                <option class="genColorList" data-colortype="2" value="50">Trans-Dark Pink</option>
                                                <option class="genColorList" data-colortype="2" value="20">Trans-Green</option>
                                                <option class="genColorList" data-colortype="2" value="15">Trans-Light Blue</option>
                                                <option class="genColorList" data-colortype="2" value="226">Trans-Light Bright Green</option>
                                                <option class="genColorList" data-colortype="2" value="221">Trans-Light Green</option>
                                                <option class="genColorList" data-colortype="2" value="164">Trans-Light Orange</option>
                                                <option class="genColorList" data-colortype="2" value="114">Trans-Light Purple</option>
                                                <option class="genColorList" data-colortype="2" value="74">Trans-Medium Blue</option>
                                                <option class="genColorList" data-colortype="2" value="16">Trans-Neon Green</option>
                                                <option class="genColorList" data-colortype="2" value="18">Trans-Neon Orange</option>
                                                <option class="genColorList" data-colortype="2" value="121">Trans-Neon Yellow</option>
                                                <option class="genColorList" data-colortype="2" value="98">Trans-Orange</option>
                                                <option class="genColorList" data-colortype="2" value="107">Trans-Pink</option>
                                                <option class="genColorList" data-colortype="2" value="51">Trans-Purple</option>
                                                <option class="genColorList" data-colortype="2" value="17">Trans-Red</option>
                                                <option class="genColorList" data-colortype="2" value="19">Trans-Yellow</option>
                                            </optgroup>
                                            <optgroup class="genColorList" data-colortype="3" label="-- Chrome/Pearl Colors">
                                                <option class="genColorList" data-colortype="3" value="57">Chrome Antique Brass</option>
                                                <option class="genColorList" data-colortype="3" value="122">Chrome Black</option>
                                                <option class="genColorList" data-colortype="3" value="52">Chrome Blue</option>
                                                <option class="genColorList" data-colortype="3" value="21">Chrome Gold</option>
                                                <option class="genColorList" data-colortype="3" value="64">Chrome Green</option>
                                                <option class="genColorList" data-colortype="3" value="82">Chrome Pink</option>
                                                <option class="genColorList" data-colortype="3" value="22">Chrome Silver</option>
                                                <option class="genColorList" data-colortype="4" value="84">Copper</option>
                                                <option class="genColorList" data-colortype="4" value="81">Flat Dark Gold</option>
                                                <option class="genColorList" data-colortype="4" value="95">Flat Silver</option>
                                                <option class="genColorList" data-colortype="4" value="78">Metal Blue</option>
                                                <option class="genColorList" data-colortype="4" value="77">Pearl Dark Gray</option>
                                                <option class="genColorList" data-colortype="4" value="115">Pearl Gold</option>
                                                <option class="genColorList" data-colortype="4" value="61">Pearl Light Gold</option>
                                                <option class="genColorList" data-colortype="4" value="66">Pearl Light Gray</option>
                                                <option class="genColorList" data-colortype="4" value="119">Pearl Very Light Gray</option>
                                                <option class="genColorList" data-colortype="4" value="83">Pearl White</option>
                                            </optgroup>
                                            <optgroup class="genColorList" data-colortype="5" label="-- Satin/Metallic/Glitter">
                                                <option class="genColorList" data-colortype="5" value="229">Satin Trans-Black</option>
                                                <option class="genColorList" data-colortype="5" value="224">Satin Trans-Dark Pink</option>
                                                <option class="genColorList" data-colortype="5" value="223">Satin Trans-Light Blue</option>
                                                <option class="genColorList" data-colortype="5" value="230">Satin Trans-Purple</option>
                                                <option class="genColorList" data-colortype="5" value="228">Satin White</option>
                                                <option class="genColorList" data-colortype="6" value="65">Metallic Gold</option>
                                                <option class="genColorList" data-colortype="6" value="70">Metallic Green</option>
                                                <option class="genColorList" data-colortype="6" value="67">Metallic Silver</option>
                                                <option class="genColorList" data-colortype="7" value="46">Glow In Dark Opaque</option>
                                                <option class="genColorList" data-colortype="7" value="118">Glow In Dark Trans</option>
                                                <option class="genColorList" data-colortype="7" value="159">Glow In Dark White</option>
                                                <option class="genColorList" data-colortype="7" value="60">Milky White</option>
                                                <option class="genColorList" data-colortype="8" value="101">Glitter Trans-Clear</option>
                                                <option class="genColorList" data-colortype="8" value="100">Glitter Trans-Dark Pink</option>
                                                <option class="genColorList" data-colortype="8" value="162">Glitter Trans-Light Blue</option>
                                                <option class="genColorList" data-colortype="8" value="163">Glitter Trans-Neon Green</option>
                                                <option class="genColorList" data-colortype="8" value="222">Glitter Trans-Orange</option>
                                                <option class="genColorList" data-colortype="8" value="102">Glitter Trans-Purple</option>
                                                <option class="genColorList" data-colortype="9" value="116">Speckle Black-Copper</option>
                                                <option class="genColorList" data-colortype="9" value="151">Speckle Black-Gold</option>
                                                <option class="genColorList" data-colortype="9" value="111">Speckle Black-Silver</option>
                                                <option class="genColorList" data-colortype="9" value="117">Speckle DBGray-Silver</option>
                                            </optgroup>
                                            <optgroup class="genColorList" data-colortype="10" label="-- Modulex Colors">
                                                <option class="genColorList" data-colortype="10" value="142">Mx Aqua Green</option>
                                                <option class="genColorList" data-colortype="10" value="128">Mx Black</option>
                                                <option class="genColorList" data-colortype="10" value="132">Mx Brown</option>
                                                <option class="genColorList" data-colortype="10" value="133">Mx Buff</option>
                                                <option class="genColorList" data-colortype="10" value="126">Mx Charcoal Gray</option>
                                                <option class="genColorList" data-colortype="10" value="149">Mx Clear</option>
                                                <option class="genColorList" data-colortype="10" value="214">Mx Foil Dark Blue</option>
                                                <option class="genColorList" data-colortype="10" value="210">Mx Foil Dark Gray</option>
                                                <option class="genColorList" data-colortype="10" value="212">Mx Foil Dark Green</option>
                                                <option class="genColorList" data-colortype="10" value="215">Mx Foil Light Blue</option>
                                                <option class="genColorList" data-colortype="10" value="211">Mx Foil Light Gray</option>
                                                <option class="genColorList" data-colortype="10" value="213">Mx Foil Light Green</option>
                                                <option class="genColorList" data-colortype="10" value="219">Mx Foil Orange</option>
                                                <option class="genColorList" data-colortype="10" value="217">Mx Foil Red</option>
                                                <option class="genColorList" data-colortype="10" value="216">Mx Foil Violet</option>
                                                <option class="genColorList" data-colortype="10" value="218">Mx Foil Yellow</option>
                                                <option class="genColorList" data-colortype="10" value="139">Mx Lemon</option>
                                                <option class="genColorList" data-colortype="10" value="124">Mx Light Bluish Gray</option>
                                                <option class="genColorList" data-colortype="10" value="125">Mx Light Gray</option>
                                                <option class="genColorList" data-colortype="10" value="136">Mx Light Orange</option>
                                                <option class="genColorList" data-colortype="10" value="137">Mx Light Yellow</option>
                                                <option class="genColorList" data-colortype="10" value="144">Mx Medium Blue</option>
                                                <option class="genColorList" data-colortype="10" value="138">Mx Ochre Yellow</option>
                                                <option class="genColorList" data-colortype="10" value="140">Mx Olive Green</option>
                                                <option class="genColorList" data-colortype="10" value="135">Mx Orange</option>
                                                <option class="genColorList" data-colortype="10" value="145">Mx Pastel Blue</option>
                                                <option class="genColorList" data-colortype="10" value="141">Mx Pastel Green</option>
                                                <option class="genColorList" data-colortype="10" value="148">Mx Pink</option>
                                                <option class="genColorList" data-colortype="10" value="130">Mx Pink Red</option>
                                                <option class="genColorList" data-colortype="10" value="129">Mx Red</option>
                                                <option class="genColorList" data-colortype="10" value="146">Mx Teal Blue</option>
                                                <option class="genColorList" data-colortype="10" value="134">Mx Terracotta</option>
                                                <option class="genColorList" data-colortype="10" value="143">Mx Tile Blue</option>
                                                <option class="genColorList" data-colortype="10" value="131">Mx Tile Brown</option>
                                                <option class="genColorList" data-colortype="10" value="127">Mx Tile Gray</option>
                                                <option class="genColorList" data-colortype="10" value="147">Mx Violet</option>
                                                <option class="genColorList" data-colortype="10" value="123">Mx White</option>
                                            </optgroup>
                                        </select>
                                    </div>
                                    <div className="col-md-2">
                                        <label for="cond">Part Condition</label>
                                        <select className="custom-select" onChange={this.handleChange} name="cond"
                                            value={this.state.addPart.cond} id="cond" aria-label="Condition - New or Used" required>
                                            <option value="">Select...</option>
                                            <option value="New">New</option>
                                            <option value="Used">Used</option>
                                            <option value="Any">Any</option>
                                        </select>
                                    </div>
                                    <div className="col-md-2">
                                        <label for="quantity">How Many Desired</label>
                                        <select className="custom-select" onChange={this.handleChange} name="quantity"
                                            value={this.state.addPart.quantity} id="quantity" aria-label="How many do you want?" required>
                                            <option value="">Select...</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="d-grid gap-2 centered mr-6">
                                    <div><small className="centered">* Color is only required for parts, not minifigures.</small></div>
                                    {this.state.addStatus}
                                    <button onClick={this.handleAdd} className="btn btn-primary back-blue text-gold"
                                        type="button">Add Part to Wanted List</button>
                                </div>
                            </form>
                            <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center py-2 mb-3 border-bottom">
                            </div>
                            <h1 className="h2 back-blue text-gold">{this.state.customer.firstName} {this.state.customer.lastName}'s Want List</h1>
                            <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
                                {this.state.wantList.map((wantedPart, index) =>
                                    <div className="col-md-3">
                                        <div className="card shadow-sm back-green text-gold">
                                            <div className="card-body centered">
                                                <div className="row mx-auto">
                                                    <div className="column ml-3">
                                                        <img src={wantedPart.imageURL} width="100px" height="100px" />
                                                    </div>
                                                    <div className="column ml-3  my-auto">
                                                        <a href="#editPart" onClick={() => this.handleClick(wantedPart)} class="btn btn-block back-blue text-gold mb-2" data-toggle="modal">Edit</a>
                                                        <div><a href="#removePart" onClick={() => this.handleClick(wantedPart)} class="btn btn-block back-blue text-gold" data-toggle="modal">Remove</a></div>

                                                    </div>
                                                </div>
                                                <p className="card-text">{wantedPart.name}</p>
                                                <p className="card-text">{wantedPart.cond} Condition</p>
                                                <p className="card-text">{wantedPart.quantity} Desired</p>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                            <hr className="mb-4"></hr>

                        </main>
                    </div>
                    {/* Beginning of removePart Modal */}
                    <div id="removePart" className="modal fade">
                        <div className="modal-dialog modal-sm">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h4 className="modal-title">Confirm Removal</h4>
                                    <button type="button" className="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                </div>
                                <div className="modal-body">
                                    <img src={this.state.selectedPart.imageURL} width="100px" height="100px" />
                                    <p>Do you want to delete this part?</p>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
                                    <button type="button" onClick={this.handleRemove} className="btn btn-primary" data-dismiss="modal">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* End of removePart Modal */}
                    {/* Beginning of editPart Modal */}
                    <div id="editPart" className="modal fade">
                        <div className="modal-dialog">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h4 className="modal-title">Update Part Information</h4>
                                    <button type="button" className="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                </div>
                                <div className="modal-body">
                                    <form>

                                        <div className="row mb-2">
                                            <div className="col-md-4">
                                                <label for="type">Part Type</label>
                                                <select className="custom-select" onChange={this.handleUpdateChange} name="type"
                                                    value={this.state.selectedPart.type} id="type" aria-label="Part type" required>
                                                    <option value="">Select from below...</option>
                                                    <option value="P">Lego Part</option>
                                                    <option value="M">Minifigure</option>
                                                </select>
                                            </div>
                                            <div className="col-md-4">
                                                <label for="pnum">Part Number</label>
                                                <input type="text" onChange={this.handleUpdateChange} id="pnum" name="number"
                                                    value={this.state.selectedPart.number} className="form-control" placeholder="Part number"
                                                    aria-label="Part Number" required />
                                            </div>
                                            {/* <div className="col-md-4">
                                                <label for="colorNum">Color Number *</label>
                                                <input type="text" onChange={this.handleUpdateChange} id="colorNum" name="colorNum"
                                                    value={this.state.selectedPart.colorNum} className="form-control" placeholder="Color Number"
                                                    aria-label="Number associated with the color" />
                                            </div> */}
                                            <div className="col-md-4">
                                                <label for="colorNum">Color *</label>
                                                <select className="custom-select" onChange={this.handleUpdateChange} name="colorNum"
                                                    value={this.state.addPart.colorNum} id="colorNum" aria-label="Choose a color" required>
                                                    <option value="">Select...</option>
                                                    <optgroup label="-- LEGO Colors">
                                                        <option class="genColorList" data-colortype="1" value="41">Aqua</option>
                                                        <option class="genColorList" data-colortype="1" value="11">Black</option>
                                                        <option class="genColorList" data-colortype="1" value="7">Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="97">Blue-Violet</option>
                                                        <option class="genColorList" data-colortype="1" value="36">Bright Green</option>
                                                        <option class="genColorList" data-colortype="1" value="105">Bright Light Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="110">Bright Light Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="103">Bright Light Yellow</option>
                                                        <option class="genColorList" data-colortype="1" value="104">Bright Pink</option>
                                                        <option class="genColorList" data-colortype="1" value="8">Brown</option>
                                                        <option class="genColorList" data-colortype="1" value="227">Clikits Lavender</option>
                                                        <option class="genColorList" data-colortype="1" value="220">Coral</option>
                                                        <option class="genColorList" data-colortype="1" value="153">Dark Azure</option>
                                                        <option class="genColorList" data-colortype="1" value="63">Dark Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="109">Dark Blue-Violet</option>
                                                        <option class="genColorList" data-colortype="1" value="85">Dark Bluish Gray</option>
                                                        <option class="genColorList" data-colortype="1" value="120">Dark Brown</option>
                                                        <option class="genColorList" data-colortype="1" value="10">Dark Gray</option>
                                                        <option class="genColorList" data-colortype="1" value="80">Dark Green</option>
                                                        <option class="genColorList" data-colortype="1" value="225">Dark Nougat</option>
                                                        <option class="genColorList" data-colortype="1" value="68">Dark Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="47">Dark Pink</option>
                                                        <option class="genColorList" data-colortype="1" value="89">Dark Purple</option>
                                                        <option class="genColorList" data-colortype="1" value="59">Dark Red</option>
                                                        <option class="genColorList" data-colortype="1" value="231">Dark Salmon</option>
                                                        <option class="genColorList" data-colortype="1" value="69">Dark Tan</option>
                                                        <option class="genColorList" data-colortype="1" value="39">Dark Turquoise</option>
                                                        <option class="genColorList" data-colortype="1" value="161">Dark Yellow</option>
                                                        <option class="genColorList" data-colortype="1" value="29">Earth Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="106">Fabuland Brown</option>
                                                        <option class="genColorList" data-colortype="1" value="160">Fabuland Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="6">Green</option>
                                                        <option class="genColorList" data-colortype="1" value="154">Lavender</option>
                                                        <option class="genColorList" data-colortype="1" value="152">Light Aqua</option>
                                                        <option class="genColorList" data-colortype="1" value="62">Light Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="86">Light Bluish Gray</option>
                                                        <option class="genColorList" data-colortype="1" value="9">Light Gray</option>
                                                        <option class="genColorList" data-colortype="1" value="38">Light Green</option>
                                                        <option class="genColorList" data-colortype="1" value="35">Light Lime</option>
                                                        <option class="genColorList" data-colortype="1" value="90">Light Nougat</option>
                                                        <option class="genColorList" data-colortype="1" value="32">Light Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="56">Light Pink</option>
                                                        <option class="genColorList" data-colortype="1" value="93">Light Purple</option>
                                                        <option class="genColorList" data-colortype="1" value="26">Light Salmon</option>
                                                        <option class="genColorList" data-colortype="1" value="40">Light Turquoise</option>
                                                        <option class="genColorList" data-colortype="1" value="44">Light Violet</option>
                                                        <option class="genColorList" data-colortype="1" value="33">Light Yellow</option>
                                                        <option class="genColorList" data-colortype="1" value="34">Lime</option>
                                                        <option class="genColorList" data-colortype="1" value="72">Maersk Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="71">Magenta</option>
                                                        <option class="genColorList" data-colortype="1" value="156">Medium Azure</option>
                                                        <option class="genColorList" data-colortype="1" value="42">Medium Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="91">Medium Brown</option>
                                                        <option class="genColorList" data-colortype="1" value="94">Medium Dark Pink</option>
                                                        <option class="genColorList" data-colortype="1" value="37">Medium Green</option>
                                                        <option class="genColorList" data-colortype="1" value="157">Medium Lavender</option>
                                                        <option class="genColorList" data-colortype="1" value="76">Medium Lime</option>
                                                        <option class="genColorList" data-colortype="1" value="150">Medium Nougat</option>
                                                        <option class="genColorList" data-colortype="1" value="31">Medium Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="73">Medium Violet</option>
                                                        <option class="genColorList" data-colortype="1" value="166">Neon Green</option>
                                                        <option class="genColorList" data-colortype="1" value="165">Neon Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="28">Nougat</option>
                                                        <option class="genColorList" data-colortype="1" value="155">Olive Green</option>
                                                        <option class="genColorList" data-colortype="1" value="4">Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="23">Pink</option>
                                                        <option class="genColorList" data-colortype="1" value="24">Purple</option>
                                                        <option class="genColorList" data-colortype="1" value="5">Red</option>
                                                        <option class="genColorList" data-colortype="1" value="88">Reddish Brown</option>
                                                        <option class="genColorList" data-colortype="1" value="27">Rust</option>
                                                        <option class="genColorList" data-colortype="1" value="25">Salmon</option>
                                                        <option class="genColorList" data-colortype="1" value="55">Sand Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="48">Sand Green</option>
                                                        <option class="genColorList" data-colortype="1" value="54">Sand Purple</option>
                                                        <option class="genColorList" data-colortype="1" value="58">Sand Red</option>
                                                        <option class="genColorList" data-colortype="1" value="87">Sky Blue</option>
                                                        <option class="genColorList" data-colortype="1" value="2">Tan</option>
                                                        <option class="genColorList" data-colortype="1" value="99">Very Light Bluish Gray</option>
                                                        <option class="genColorList" data-colortype="1" value="49">Very Light Gray</option>
                                                        <option class="genColorList" data-colortype="1" value="96">Very Light Orange</option>
                                                        <option class="genColorList" data-colortype="1" value="43">Violet</option>
                                                        <option class="genColorList" data-colortype="1" value="1">White</option>
                                                        <option class="genColorList" data-colortype="1" value="3">Yellow</option>
                                                        <option class="genColorList" data-colortype="1" value="158">Yellowish Green</option>
                                                    </optgroup>
                                                    <optgroup class="genColorList" data-colortype="2" label="-- Transparent Colors">
                                                        <option class="genColorList" data-colortype="2" value="113">Trans-Aqua</option>
                                                        <option class="genColorList" data-colortype="2" value="13">Trans-Black</option>
                                                        <option class="genColorList" data-colortype="2" value="108">Trans-Bright Green</option>
                                                        <option class="genColorList" data-colortype="2" value="12">Trans-Clear</option>
                                                        <option class="genColorList" data-colortype="2" value="14">Trans-Dark Blue</option>
                                                        <option class="genColorList" data-colortype="2" value="50">Trans-Dark Pink</option>
                                                        <option class="genColorList" data-colortype="2" value="20">Trans-Green</option>
                                                        <option class="genColorList" data-colortype="2" value="15">Trans-Light Blue</option>
                                                        <option class="genColorList" data-colortype="2" value="226">Trans-Light Bright Green</option>
                                                        <option class="genColorList" data-colortype="2" value="221">Trans-Light Green</option>
                                                        <option class="genColorList" data-colortype="2" value="164">Trans-Light Orange</option>
                                                        <option class="genColorList" data-colortype="2" value="114">Trans-Light Purple</option>
                                                        <option class="genColorList" data-colortype="2" value="74">Trans-Medium Blue</option>
                                                        <option class="genColorList" data-colortype="2" value="16">Trans-Neon Green</option>
                                                        <option class="genColorList" data-colortype="2" value="18">Trans-Neon Orange</option>
                                                        <option class="genColorList" data-colortype="2" value="121">Trans-Neon Yellow</option>
                                                        <option class="genColorList" data-colortype="2" value="98">Trans-Orange</option>
                                                        <option class="genColorList" data-colortype="2" value="107">Trans-Pink</option>
                                                        <option class="genColorList" data-colortype="2" value="51">Trans-Purple</option>
                                                        <option class="genColorList" data-colortype="2" value="17">Trans-Red</option>
                                                        <option class="genColorList" data-colortype="2" value="19">Trans-Yellow</option>
                                                    </optgroup>
                                                    <optgroup class="genColorList" data-colortype="3" label="-- Chrome/Pearl Colors">
                                                        <option class="genColorList" data-colortype="3" value="57">Chrome Antique Brass</option>
                                                        <option class="genColorList" data-colortype="3" value="122">Chrome Black</option>
                                                        <option class="genColorList" data-colortype="3" value="52">Chrome Blue</option>
                                                        <option class="genColorList" data-colortype="3" value="21">Chrome Gold</option>
                                                        <option class="genColorList" data-colortype="3" value="64">Chrome Green</option>
                                                        <option class="genColorList" data-colortype="3" value="82">Chrome Pink</option>
                                                        <option class="genColorList" data-colortype="3" value="22">Chrome Silver</option>
                                                        <option class="genColorList" data-colortype="4" value="84">Copper</option>
                                                        <option class="genColorList" data-colortype="4" value="81">Flat Dark Gold</option>
                                                        <option class="genColorList" data-colortype="4" value="95">Flat Silver</option>
                                                        <option class="genColorList" data-colortype="4" value="78">Metal Blue</option>
                                                        <option class="genColorList" data-colortype="4" value="77">Pearl Dark Gray</option>
                                                        <option class="genColorList" data-colortype="4" value="115">Pearl Gold</option>
                                                        <option class="genColorList" data-colortype="4" value="61">Pearl Light Gold</option>
                                                        <option class="genColorList" data-colortype="4" value="66">Pearl Light Gray</option>
                                                        <option class="genColorList" data-colortype="4" value="119">Pearl Very Light Gray</option>
                                                        <option class="genColorList" data-colortype="4" value="83">Pearl White</option>
                                                    </optgroup>
                                                    <optgroup class="genColorList" data-colortype="5" label="-- Satin/Metallic/Glitter">
                                                        <option class="genColorList" data-colortype="5" value="229">Satin Trans-Black</option>
                                                        <option class="genColorList" data-colortype="5" value="224">Satin Trans-Dark Pink</option>
                                                        <option class="genColorList" data-colortype="5" value="223">Satin Trans-Light Blue</option>
                                                        <option class="genColorList" data-colortype="5" value="230">Satin Trans-Purple</option>
                                                        <option class="genColorList" data-colortype="5" value="228">Satin White</option>
                                                        <option class="genColorList" data-colortype="6" value="65">Metallic Gold</option>
                                                        <option class="genColorList" data-colortype="6" value="70">Metallic Green</option>
                                                        <option class="genColorList" data-colortype="6" value="67">Metallic Silver</option>
                                                        <option class="genColorList" data-colortype="7" value="46">Glow In Dark Opaque</option>
                                                        <option class="genColorList" data-colortype="7" value="118">Glow In Dark Trans</option>
                                                        <option class="genColorList" data-colortype="7" value="159">Glow In Dark White</option>
                                                        <option class="genColorList" data-colortype="7" value="60">Milky White</option>
                                                        <option class="genColorList" data-colortype="8" value="101">Glitter Trans-Clear</option>
                                                        <option class="genColorList" data-colortype="8" value="100">Glitter Trans-Dark Pink</option>
                                                        <option class="genColorList" data-colortype="8" value="162">Glitter Trans-Light Blue</option>
                                                        <option class="genColorList" data-colortype="8" value="163">Glitter Trans-Neon Green</option>
                                                        <option class="genColorList" data-colortype="8" value="222">Glitter Trans-Orange</option>
                                                        <option class="genColorList" data-colortype="8" value="102">Glitter Trans-Purple</option>
                                                        <option class="genColorList" data-colortype="9" value="116">Speckle Black-Copper</option>
                                                        <option class="genColorList" data-colortype="9" value="151">Speckle Black-Gold</option>
                                                        <option class="genColorList" data-colortype="9" value="111">Speckle Black-Silver</option>
                                                        <option class="genColorList" data-colortype="9" value="117">Speckle DBGray-Silver</option>
                                                    </optgroup>
                                                    <optgroup class="genColorList" data-colortype="10" label="-- Modulex Colors">
                                                        <option class="genColorList" data-colortype="10" value="142">Mx Aqua Green</option>
                                                        <option class="genColorList" data-colortype="10" value="128">Mx Black</option>
                                                        <option class="genColorList" data-colortype="10" value="132">Mx Brown</option>
                                                        <option class="genColorList" data-colortype="10" value="133">Mx Buff</option>
                                                        <option class="genColorList" data-colortype="10" value="126">Mx Charcoal Gray</option>
                                                        <option class="genColorList" data-colortype="10" value="149">Mx Clear</option>
                                                        <option class="genColorList" data-colortype="10" value="214">Mx Foil Dark Blue</option>
                                                        <option class="genColorList" data-colortype="10" value="210">Mx Foil Dark Gray</option>
                                                        <option class="genColorList" data-colortype="10" value="212">Mx Foil Dark Green</option>
                                                        <option class="genColorList" data-colortype="10" value="215">Mx Foil Light Blue</option>
                                                        <option class="genColorList" data-colortype="10" value="211">Mx Foil Light Gray</option>
                                                        <option class="genColorList" data-colortype="10" value="213">Mx Foil Light Green</option>
                                                        <option class="genColorList" data-colortype="10" value="219">Mx Foil Orange</option>
                                                        <option class="genColorList" data-colortype="10" value="217">Mx Foil Red</option>
                                                        <option class="genColorList" data-colortype="10" value="216">Mx Foil Violet</option>
                                                        <option class="genColorList" data-colortype="10" value="218">Mx Foil Yellow</option>
                                                        <option class="genColorList" data-colortype="10" value="139">Mx Lemon</option>
                                                        <option class="genColorList" data-colortype="10" value="124">Mx Light Bluish Gray</option>
                                                        <option class="genColorList" data-colortype="10" value="125">Mx Light Gray</option>
                                                        <option class="genColorList" data-colortype="10" value="136">Mx Light Orange</option>
                                                        <option class="genColorList" data-colortype="10" value="137">Mx Light Yellow</option>
                                                        <option class="genColorList" data-colortype="10" value="144">Mx Medium Blue</option>
                                                        <option class="genColorList" data-colortype="10" value="138">Mx Ochre Yellow</option>
                                                        <option class="genColorList" data-colortype="10" value="140">Mx Olive Green</option>
                                                        <option class="genColorList" data-colortype="10" value="135">Mx Orange</option>
                                                        <option class="genColorList" data-colortype="10" value="145">Mx Pastel Blue</option>
                                                        <option class="genColorList" data-colortype="10" value="141">Mx Pastel Green</option>
                                                        <option class="genColorList" data-colortype="10" value="148">Mx Pink</option>
                                                        <option class="genColorList" data-colortype="10" value="130">Mx Pink Red</option>
                                                        <option class="genColorList" data-colortype="10" value="129">Mx Red</option>
                                                        <option class="genColorList" data-colortype="10" value="146">Mx Teal Blue</option>
                                                        <option class="genColorList" data-colortype="10" value="134">Mx Terracotta</option>
                                                        <option class="genColorList" data-colortype="10" value="143">Mx Tile Blue</option>
                                                        <option class="genColorList" data-colortype="10" value="131">Mx Tile Brown</option>
                                                        <option class="genColorList" data-colortype="10" value="127">Mx Tile Gray</option>
                                                        <option class="genColorList" data-colortype="10" value="147">Mx Violet</option>
                                                        <option class="genColorList" data-colortype="10" value="123">Mx White</option>
                                                    </optgroup>
                                                </select>
                                            </div>
                                        </div>
                                        <div className="row mb-2">
                                            <div className="col-md-4">
                                                <label for="cond">Part Condition</label>
                                                <select className="custom-select" onChange={this.handleUpdateChange} name="cond"
                                                    value={this.state.selectedPart.cond} id="cond" aria-label="Condition - New or Used" required>
                                                    <option value="">Select from below...</option>
                                                    <option value="New">New</option>
                                                    <option value="Used">Used</option>
                                                    <option value="Any">Any</option>
                                                </select>
                                            </div>
                                            <div className="col-md-4">
                                                <label for="quantity">How Many Desired</label>
                                                <select className="custom-select" onChange={this.handleUpdateChange} name="quantity"
                                                    value={this.state.selectedPart.quantity} id="quantity" aria-label="How many do you want?" required>
                                                    <option value="">Select from below...</option>
                                                    <option value="1">1</option>
                                                    <option value="2">2</option>
                                                    <option value="3">3</option>
                                                    <option value="4">4</option>
                                                    <option value="5">5</option>
                                                    <option value="6">6</option>
                                                    <option value="7">7</option>
                                                    <option value="8">8</option>
                                                    <option value="9">9</option>
                                                    <option value="10">10</option>
                                                </select>
                                            </div>
                                            <small className="ml-3">* Color number is only required for parts, not minifigures.</small>
                                        </div>
                                    </form>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
                                    <button type="button" onClick={this.handleUpdate} className="btn btn-primary" data-dismiss="modal">Update</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* End of editPart Modal */}
                </div>
            </div>
        );
    }
}

export default Home;